package server_instance;

public abstract class ServerInstanceManagement {
    protected int server_port;

    public ServerInstanceManagement(int server_port) {
        this.server_port = server_port;
    }

    public abstract void createServerInstance();
    public abstract void closeServerInstance();
    public abstract void restartServerInstance();
    public abstract Integer[] getBranchCoverageVector();
    public abstract Integer[] getStatementCoverageVector();
    public abstract void resetCoverage();
}
