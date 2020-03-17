package server_instance;

import java.io.IOException;

public abstract class ServerInstanceManagement {
    protected int server_port;

    public ServerInstanceManagement(int server_port) {
        this.server_port = server_port;
    }

    public abstract void createServerInstance();
    public abstract void closeServerInstance();
    public abstract void restartServerInstance();
    public abstract Integer[] gerCoverageVector();
    public abstract void resetCoverage();
}
