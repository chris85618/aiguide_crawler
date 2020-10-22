package server_instance.codeCoverage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class IstanbulCodeCoverage implements CodeCoverage {

    public IstanbulCodeCoverage(){}

    @Override
    public void setCoverageVector(Integer[] branchCoverageVector) {

    }

    @Override
    public Integer[] getCoverageVector() {
        return new Integer[0];
    }

    @Override
    public int getCoveredAmount() {
        return 0;
    }

    @Override
    public int getCoverageVectorSize() {
        return 0;
    }

    @Override
    public double getCoverage() {
        return 0;
    }

    @Override
    public void merge(CodeCoverage codeCoverage) {

    }

    @Override
    public void xor(CodeCoverage codeCoverage) {

    }
}
