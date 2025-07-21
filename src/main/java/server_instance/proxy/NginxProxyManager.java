package server_instance.proxy;

import util.CommandHelper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class NginxProxyManager extends ProxyManager {

    private final String proxyContainerName = "dynamic-proxy-nginx";
    private final int hostPort;
    private final Path tempConfPath;
    private String templateContent;
    private boolean isRunning = false;

    public NginxProxyManager(int hostPort) {
        this.hostPort = hostPort;
        try {
            this.tempConfPath = Files.createTempDirectory("nginx-conf-");
            this.tempConfPath.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp dir for nginx config", e);
        }
        this.start();
    }
    
    public void start() {
        System.out.println("[Proxy] Starting Nginx Proxy...");
        writeInitialConfig();
        startNginxContainer();
        if (isRunning) {
            System.out.println("[Proxy] Nginx proxy is running and accessible on http://localhost:" + hostPort);
        } else {
            System.err.println("[Proxy] Nginx proxy failed to start.");
        }
    }

    @Override
    public void updateBackend(Optional<String> backendEndpoint) {
        loadTemplate();
        String upstreamServers;

        if (backendEndpoint.isPresent()) {
            String endpoint = backendEndpoint.get();
            upstreamServers = "    server " + endpoint + ";";
            System.out.println("[Proxy] Updating Nginx to target: " + endpoint);
        } else {
            upstreamServers = "    server 127.0.0.1:12345 down; # Placeholder to prevent config errors";
            System.out.println("[Proxy] Updating Nginx to target: NONE");
        }
        String newConfigContent = templateContent.replace("##UPSTREAM_SERVERS##", upstreamServers);

        try {
            Path confFile = tempConfPath.resolve("nginx.conf");
            Files.write(confFile, newConfigContent.getBytes(StandardCharsets.UTF_8));
            reloadNginx();
        } catch (IOException e) {
            System.err.println("[Proxy] Failed to write Nginx config file: " + e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        System.out.println("[Proxy] Shutting down Nginx proxy...");
        if (isRunning) {
            CommandHelper.executeCommand("docker", "stop", proxyContainerName);
            isRunning = false;
            System.out.println("[Proxy] Container " + proxyContainerName + " stopped.");
        }
        try {
            Files.deleteIfExists(tempConfPath.resolve("nginx.conf"));
            Files.deleteIfExists(tempConfPath);
        } catch (IOException e) {
            System.err.println("[Proxy] Could not clean up temporary config file.");
        }
    }

    private void startNginxContainer() {
        Path confFile = tempConfPath.resolve("nginx.conf");
        CommandHelper.executeCommand("docker", "rm", "-fv", proxyContainerName);
        String containerId = CommandHelper.executeCommand(
                "docker", "run",
                "-d",
                "--name", proxyContainerName,
                "--network=host",
                "-v", confFile.toAbsolutePath().toString() + ":/etc/nginx/nginx.conf",
                "nginx:latest"
        );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        this.isRunning = containerId != null && !containerId.trim().isEmpty();
    }
    
    private void reloadNginx() {
        if (isRunning) {
             System.out.println("[Proxy] Reloading Nginx configuration...");
             CommandHelper.executeCommand("docker", "exec", proxyContainerName, "nginx", "-s", "reload");
        }
    }

    private void loadTemplate() {
        if (this.templateContent != null)
            return;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("nginx.conf.template")) {
            if (is == null) {
                throw new IOException("nginx.conf.template not found in classpath resources.");
            }
            this.templateContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load nginx.conf.template", e);
        }
    }

    private void writeInitialConfig() {
        updateBackend(Optional.empty());
    }
}
