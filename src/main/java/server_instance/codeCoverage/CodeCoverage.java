package server_instance.codeCoverage;

public interface CodeCoverage {
    void setCoverageVector(Integer[] branchCoverageVector);
    Integer[] getCoverageVector();
    int getCoveredAmount();
    int getCoverageVectorSize();
    double getCoverage();

    void merge(CodeCoverage codeCoverage);
    void xor(CodeCoverage codeCoverage);
}
