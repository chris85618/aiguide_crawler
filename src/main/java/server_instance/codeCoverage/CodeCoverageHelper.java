package server_instance.codeCoverage;

public class CodeCoverageHelper {

    private CodeCoverageCollector codeCoverageCollector;
    private CodeCoverage branchCoverage;
    private CodeCoverage statementCoverage;

    public CodeCoverageHelper(CodeCoverageCollector codeCoverageCollector){
        this.codeCoverageCollector = codeCoverageCollector;
        this.resetCumulativeCoverage();
    }

    public void recordCoverage(){
        final CodeCoverage branchCoverage = this.getBranchCoverage();
        final CodeCoverage statementCoverage = this.getStatementCoverage();
        this.mergeCoverage(statementCoverage, branchCoverage);
    }

    public void mergeCoverage(final CodeCoverage statementCoverage, final CodeCoverage branchCoverage){
        if(!this.isCodeCoverageRecorded()){
            this.branchCoverage = branchCoverage;
            this.statementCoverage = statementCoverage;
        }
        else{
            this.branchCoverage.merge(branchCoverage);
            this.statementCoverage.merge(statementCoverage);
        }
    }

    public void resetCumulativeCoverage(){
        this.branchCoverage = null;
        this.statementCoverage = null;
    }

    public CodeCoverage getBranchCoverage(){
        return this.codeCoverageCollector.getBranchCoverage();
    }

    public CodeCoverage getStatementCoverage(){
        return this.codeCoverageCollector.getStatementCoverage();
    }

    public CodeCoverage getCumulativeBranchCoverage(){
        return this.branchCoverage;
    }

    public CodeCoverage getCumulativeStatementCoverage(){
        return this.statementCoverage;
    }

    public void resetCoverage(){
        this.codeCoverageCollector.resetCoverage();
    }

    private Boolean isCodeCoverageRecorded(){
        return (this.branchCoverage != null) || (this.statementCoverage != null);
    }
}
