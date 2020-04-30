package crawler;

import ntut.edu.aiguide.crawljax.plugins.ServerInstanceManagement;

import java.util.Arrays;

public class ServerInstanceAdapter implements ServerInstanceManagement {
    private final server_instance.ServerInstanceManagement serverInstanceManagement;
    private Integer[] totalCoverage = null;

    public ServerInstanceAdapter(server_instance.ServerInstanceManagement serverInstanceManagement) {
        this.serverInstanceManagement = serverInstanceManagement;
    }

    @Override
    public void recordCoverage() {
        if(totalCoverage == null) totalCoverage = serverInstanceManagement.getCoverageVector();
        else {
            Integer[] currentCoverage = serverInstanceManagement.getCoverageVector();
            for (int i = 0; i < totalCoverage.length; i++)
                if (!totalCoverage[i].equals(currentCoverage[i]))
                    totalCoverage[i] = 300;
        }
        int counter = 0;
        for(int i: totalCoverage){
            if(i == 300) counter++;
        }
        System.out.println("Total coverage is: " + counter);
    }

    void resetRecordCoverage() {
        Arrays.fill(totalCoverage, 0);
    }

    Integer[] getTotalCoverage() {
        return totalCoverage;
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
