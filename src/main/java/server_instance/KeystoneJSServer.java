package server_instance;

import server_instance.codeCoverage.CodeCoverageCollector;
import server_instance.codeCoverage.CodeCoverageHelper;
import server_instance.codeCoverage.IstanbulCodeCoverageCollector;
import util.CommandHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class KeystoneJSServer extends ServerInstanceManagement {
    private final int MAXIMUM_WAITING_COUNT = 120;
    private final String dockerFolder = "./src/main/java/server_instance/dockerFile/";
    private final String compose_id;
    private String compose_file;

    public KeystoneJSServer(String appName, int server_port) {
        super(appName, server_port);
        this.compose_id = appName + "-" + server_port;
        createDockerComposeFile();
        copyVE();
        CodeCoverageCollector codeCoverageCollector = new IstanbulCodeCoverageCollector(server_port);
        this.codeCoverageHelper = new CodeCoverageHelper(codeCoverageCollector);
    }

    private void createDockerComposeFile() {
        createDockerFileFolder();
        String compose_file_content =
                "version: '3.4'\n" +
                "services:\n" +
                "  keystonejs_with_coverage_%d:\n" +
                "    image: ntutselab/keystonejs_with_coverage\n" +
                "    ports:\n" +
                "      - '%d:3000'\n" +
                "    links:\n" +
                "      - nameOfMongoDB\n" +
                "    environment:\n" +
                "      - MONGO_URI=mongodb://nameOfMongoDB:27017/\n" +
                "    healthcheck:\n" +
                "      test: [\"CMD-SHELL\", \"node -e \\\"require('http').request('http://localhost:3000/keystone', {timeout: 3000}, res => process.exit(res.statusCode < 400 ? 0 : 1)).on('error', () => process.exit(1)).end()\\\"\"]\n" +
                "      interval: 2s\n" +
                "      timeout: 1s\n" +
                "      retries: 25\n" +
                "      start_period: 460s\n" +
                "    depends_on:\n" +
                "      nameOfMongoDB:\n" +
                "        condition: service_healthy\n" +
                "  nameOfMongoDB:\n" +
                "    image: ntutselab/mongo\n" +
                "    tmpfs:\n" +
                "      - /data/db\n" +
                "    healthcheck:\n" +
                "      test: [\"CMD-SHELL\", \"echo 'db.runCommand(\\\"{ ping: 1 }\\\").ok' | mongo localhost:27017/test --quiet\"]\n" +
                "      interval: 10s\n" +
                "      timeout: 5s\n" +
                "      retries: 5\n" +
                "      start_period: 10s\n";
        compose_file_content = String.format(compose_file_content, server_port % 3000, server_port);
        compose_file = dockerFolder + "docker_compose_keystonejs_" + (server_port % 3000) + ".yml";
        try {
            FileWriter fw = new FileWriter(compose_file);
            fw.write(compose_file_content);
            fw.flush();
            fw.close();
        }catch (IOException e){
            System.out.println("Error!!!");
            e.printStackTrace();
            throw new RuntimeException("Write docker file error!!");
        }
    }

    private void createDockerFileFolder() {
        File file = new File("./src/main/java/server_instance/dockerFile");
        boolean bool = file.mkdir();
        if(bool){
            System.out.println("Directory created successfully");
        }else{
            System.out.println("Folder is exist, not going to create it...");
        }
    }

    private void findBusyProcessAndKillIt() {
        String containerID = findBusyProcess();
        killBusyProcess(containerID);
    }

    private String findBusyProcess() {
        String containerID = CommandHelper.executeCommand("docker", "compose", "-p", this.compose_id, "-f", compose_file, "ps", "-q");
        System.out.println("find the container id is :" + containerID);
        return containerID;
    }

    private void killBusyProcess(String containerID) {
        String fixDeviceErrorScript = dockerFolder + "find-busy-mnt.sh";
        if(System.getProperty("os.name").toLowerCase().matches("(.*)windows(.*)")){
            CommandHelper.executeCommand("powershell.exe", fixDeviceErrorScript, containerID);
        }
        else {
            CommandHelper.executeCommand("sh", fixDeviceErrorScript, containerID);
        }
    }

    @Override
    public void createServerInstance() {
        int waitingCount = 0;
        createServer();
        String url = "http://127.0.0.1:%d";
        while(!isServerActive(String.format(url, server_port), 200)) {
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
            waitingCount += 1;
            if(waitingCount == MAXIMUM_WAITING_COUNT){
                findBusyProcessAndKillIt();
                recreateTimeOffManagement();
            }
            else if(waitingCount == MAXIMUM_WAITING_COUNT * 2) throw new RuntimeException("Something went wrong when creating NodeBB...");
        }
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void recreateTimeOffManagement() {
        System.out.println("recreate Server");
        closeServerInstance();
        createServer();
    }

    private void createServer() {
        long startTime = System.nanoTime();
        CommandHelper.executeCommand("docker", "compose", "-p", this.compose_id, "-f", compose_file, "up", "-d", "--wait");
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1000000000.0;
        System.out.println("\nServer Port is " + server_port + ", Starting server instance waiting time is :" + timeElapsed);
    }

    private boolean isServerClosed() {
        // e.g. NAME                                      IMAGE                                COMMAND                  SERVICE                      CREATED          STATUS                      PORTS
        //      dockerfile-keystonejs_with_coverage_1-1   ntutselab/keystonejs_with_coverage   "/bin/sh -c 'node ke…"   keystonejs_with_coverage_1   17 minutes ago   Exited (1) 16 minutes ago   
        //      dockerfile-nameOfMongoDB-1                ntutselab/mongo                      "docker-entrypoint.s…"   nameOfMongoDB                17 minutes ago   Up 17 minutes (healthy)     0.0.0.0:27017->27017/tcp, [::]:27017->27017/tcp
        final int totalActiveContainers = CommandHelper.executeCommand("docker", "compose", "-p", this.compose_id, "-f", compose_file, "ps", "-a").split("\\r\\n|\\r|\\n", -1).length - 1;
        return totalActiveContainers <= 0;
    }

    @Override
    public void closeServerInstance() {
        String url = "http://127.0.0.1:%d";
        long startTime = System.nanoTime();
        while(!this.isServerClosed()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            CommandHelper.executeCommand("docker", "compose", "-p", this.compose_id, "-f", compose_file, "rm", "-svf");
        }
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1000000000.0;
        System.out.println("\nClosing server instance waiting time is :" + timeElapsed);
    }

    @Override
    public void restartServerInstance() {
        closeServerInstance();
        createServerInstance();
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void resetCoverage() {
        codeCoverageHelper.resetCoverage();
    }

    public boolean isServerActive(String url, int expectedStatusCode) {
        int httpStatusCode = getResponseStatusCode(url);
        return httpStatusCode == expectedStatusCode;
    }

    private int getResponseStatusCode(String url) {
        int code;
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)targetUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
        }
        catch (UnknownHostException e){
            code = -1;
        }
        catch (SocketException e){
            code = -2;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unknown response status code!!");
        }
        return code;
    }

    private Integer[] covertToOneHot(Integer[] coverageVector) {
        for(int i = 0; i < coverageVector.length; i++){
            if(coverageVector[i] != 0) coverageVector[i] = 300;
        }
        return  coverageVector;
    }
}
