package server_instance;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerInstanceManagement {
    protected int server_port;
    protected String appName;

    public ServerInstanceManagement(String appName, int server_port) {
        this.appName = appName;
        this.server_port = server_port;
    }

    public abstract void createServerInstance();
    public abstract void closeServerInstance();
    public abstract void restartServerInstance();
    public abstract String getAppName();
    public abstract Integer[] getBranchCoverageVector();
    public abstract Integer[] getStatementCoverageVector();
    public abstract Integer[] getTotalBranchCoverageVector();
    public abstract Integer[] getTotalStatementCoverageVector();
    public abstract void recordCoverage();
    public abstract void resetCoverage();
    public abstract void resetTotalCoverage();

    protected void copyVE() {
        String source = "./variableElement/data/" + appName + "/variableElementList.json";
        try {
            File src = new File(source), dst = new File("./variableElement/variableElementList.json");
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            System.out.println("Not fount variableElementList...");
        }
    }

    public Map<String, Integer> getTotalCoverage() {
        Map<String, Integer> summary = new HashMap<>();
        int coverage_counter = 0;
        for(int i: getTotalStatementCoverageVector()) if(i != 0) coverage_counter++;
        summary.put("statement", coverage_counter);
        coverage_counter = 0;
        for(int i: getTotalBranchCoverageVector()) if(i != 0) coverage_counter++;
        summary.put("branch", coverage_counter);
        return summary;
    }
}
