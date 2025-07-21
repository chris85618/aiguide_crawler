package server_instance;

import server_instance.codeCoverage.CodeCoverage;
import server_instance.codeCoverage.CodeCoverageCollector;
import server_instance.codeCoverage.CodeCoverageHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerInstanceManagement {
    protected int server_port;
    protected String appName;
    protected CodeCoverageHelper codeCoverageHelper;

    public ServerInstanceManagement(String appName, int server_port) {
        this.appName = appName;
        this.server_port = server_port;
        this.codeCoverageHelper = null;
    }

    public abstract void createServerInstance();
    public abstract void closeServerInstance();
    public abstract void restartServerInstance();

    public String getAppName() {
        return appName;
    }

    public Integer[] getBranchCoverageVector(){
        return this.getBranchCoverage().getCodeCoverageVector().toArray(new Integer[0]);
    }

    public Integer[] getStatementCoverageVector(){
        return this.getStatementCoverage().getCodeCoverageVector().toArray(new Integer[0]);
    }

    public Integer[] getTotalBranchCoverageVector(){
        return this.getCumulativeBranchCoverage().getCodeCoverageVector().toArray(new Integer[0]);
    }

    public Integer[] getTotalStatementCoverageVector(){
        return this.getCumulativeStatementCoverage().getCodeCoverageVector().toArray(new Integer[0]);
    }

    public CodeCoverage getBranchCoverage(){
        return this.codeCoverageHelper.getBranchCoverage();
    }

    public CodeCoverage getStatementCoverage(){
        return this.codeCoverageHelper.getStatementCoverage();
    }

    public CodeCoverage getCumulativeBranchCoverage() {
        return this.codeCoverageHelper.getCumulativeBranchCoverage();
    }

    public CodeCoverage getCumulativeStatementCoverage() {
        return this.codeCoverageHelper.getCumulativeStatementCoverage();
    }

    public void recordCoverage(){
        codeCoverageHelper.recordCoverage();
    }

    public void mergeCoverage(final CodeCoverage statementCoverage, final CodeCoverage branchCoverage){
        codeCoverageHelper.mergeCoverage(statementCoverage, branchCoverage);
    }

    public void resetTotalCoverage(){
        codeCoverageHelper.resetCumulativeCoverage();
    }

    public void resetCoverage() {
        codeCoverageHelper.resetCoverage();
    }

    protected void copyVE() {
        String source = "./variableElement/data/" + appName + "/variableElementList.json";
        try {
            File src = new File(source), dst = new File("./variableElement/variableElementList.json");
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            System.out.println("Not fount variableElementList...");
        }
    }

    public Map<String, CodeCoverage> getTotalCoverage() {
        Map<String, CodeCoverage> summary = new HashMap<>();
        summary.put("branch", this.codeCoverageHelper.getCumulativeBranchCoverage());
        summary.put("statement", this.codeCoverageHelper.getCumulativeStatementCoverage());
        return summary;
    }

    public int getServerPort() {
        return this.server_port;
    }

    public CodeCoverageHelper getCodeCoverageHelper() {
        return this.codeCoverageHelper;
    }
}
