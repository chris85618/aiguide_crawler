package server_instance.codeCoverage;

public abstract class CodeCoverageHelper {

    private CodeCoverageCollector codeCoverageCollector;

    public CodeCoverageHelper(CodeCoverageCollector codeCoverageCollector){
        this.codeCoverageCollector = codeCoverageCollector;
    }

    Integer[]  getBranchCoverageVector(){
        return codeCoverageCollector.getBranchCoverageVector();
    }

    Integer[] getStatementCoverageVector(){
        return codeCoverageCollector.getStatementCoverageVector();
    }

    void resetCoverage(){
        codeCoverageCollector.resetCoverage();
    }

    public abstract int getBranchCoverageAmount();
    public abstract String getBranchCoverage();

    public abstract int getStatementCoverageAmount();
    public abstract String getStatementCoverage();

    public abstract void recordCoverage();
    public abstract void resetCumulativeCoverage();

    public abstract Integer[] getCumulativeBranchCoverageVector();
    public abstract int getCumulativeBranchCoverageAmount();
    public abstract String getCumulativeBranchCoverage();

    public abstract Integer[] getCumulativeStatementCoverageVector();
    public abstract int getCumulativeStatementCoverageAmount();
    public abstract String getCumulativeStatementCoverage();

}
