package server_instance.codeCoverage;

public interface CodeCoverageCollector {
    Integer[] getBranchCoverageVector();

    Integer[] getStatementCoverageVector();

    void resetCoverage();

    void recordCoverage();

    Integer[] getTotalBranchCoverageVector();

    Integer[] getTotalStatementCoverageVector();

    void resetTotalCoverage();
}
