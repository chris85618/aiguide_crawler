package server_instance.codeCoverage;

public interface CodeCoverage {
    void setBranchCoverageVector(Integer[] branchCoverageVector);
    void setStatementCoverageVector(Integer[] statementCoverageVector);

    Integer[] getBranchCoverageVector();
    Integer[] getStatementCoverageVector();

    int getBranchCoveredAmount();
    int getStatementCoveredAmount();

    int getBranchCoverageSize();
    int getStatementCoverageSize();

    double getBranchCoverage();
    double getStatementCoverage();

    void merge();

}
