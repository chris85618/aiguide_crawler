package crawler;

import ntut.edu.aiguide.crawljax.plugins.ServerInstanceManagement;

import java.util.Arrays;

public class ServerInstanceAdapter implements ServerInstanceManagement {
    private final server_instance.ServerInstanceManagement serverInstanceManagement;
    private Integer[] totalBranchCoverage = null;
    private Integer[] totalStatementCoverage = null;

    public ServerInstanceAdapter(server_instance.ServerInstanceManagement serverInstanceManagement) {
        this.serverInstanceManagement = serverInstanceManagement;
    }

    @Override
    public void recordCoverage() {
        if(totalBranchCoverage == null || totalStatementCoverage == null) {
            totalStatementCoverage = serverInstanceManagement.getStatementCoverageVector();
            totalBranchCoverage = serverInstanceManagement.getBranchCoverageVector();
        }
        else {
            Integer[] currentStatementCoverage = serverInstanceManagement.getStatementCoverageVector();
            for (int i = 0; i < totalStatementCoverage.length; i++) {
                if (!totalStatementCoverage[i].equals(currentStatementCoverage[i])) {
                    totalStatementCoverage[i] = 300;
                }
            }

            Integer[] currentBranchCoverage = serverInstanceManagement.getBranchCoverageVector();
            for (int i = 0; i < totalBranchCoverage.length; i++)
                if (!totalBranchCoverage[i].equals(currentBranchCoverage[i])) {
                    totalBranchCoverage[i] = 300;
                }
        }
    }

    void resetRecordCoverage() {
        Arrays.fill(totalStatementCoverage, 0);
        Arrays.fill(totalBranchCoverage, 0);
    }

    Integer[] getTotalStatementCoverage() {
        return totalStatementCoverage;
    }

    Integer[] getTotalBranchCoverage() {
        return totalBranchCoverage;
    }

    @Override
    public void createServerInstance() {
        serverInstanceManagement.createServerInstance();
    }

    @Override
    public void closeServerInstance() {
        serverInstanceManagement.closeServerInstance();
    }
}
