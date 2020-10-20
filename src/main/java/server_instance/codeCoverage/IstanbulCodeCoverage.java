package server_instance.codeCoverage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class IstanbulCodeCoverage implements CodeCoverage {

    private Integer[] branchCoverageVector = null;
    private Integer[] statementCoverageVector = null;

    public IstanbulCodeCoverage(){}

    public IstanbulCodeCoverage(Integer[] statementCoverageVector, Integer[] branchCoverageVector){
        this.statementCoverageVector = statementCoverageVector;
        this.branchCoverageVector = branchCoverageVector;
    }

    @Override
    public void setBranchCoverageVector(Integer[] branchCoverageVector) {
        this.branchCoverageVector = branchCoverageVector;
    }

    @Override
    public void setStatementCoverageVector(Integer[] statementCoverageVector) {
        this.statementCoverageVector = statementCoverageVector;
    }

    @Override
    public Integer[] getBranchCoverageVector() {
        return this.branchCoverageVector;
    }

    @Override
    public Integer[] getStatementCoverageVector() {
        return this.statementCoverageVector;
    }

    @Override
    public int getBranchCoveredAmount() {
        return this.getFilterZeroArray(this.branchCoverageVector).length;
    }

    @Override
    public int getStatementCoveredAmount() {
        return this.getFilterZeroArray(this.statementCoverageVector).length;
    }

    @Override
    public int getBranchCoverageSize() {
        return this.branchCoverageVector.length;
    }

    @Override
    public int getStatementCoverageSize() {
        return this.statementCoverageVector.length;
    }

    @Override
    public double getBranchCoverage() {
        return 1.0 * this.getBranchCoveredAmount() / this.getBranchCoverageSize();
    }

    @Override
    public double getStatementCoverage() {
        return 1.0 * this.getStatementCoveredAmount() / this.getStatementCoverageSize();
    }

    @Override
    public void merge() {
    }

    private Integer[] getFilterZeroArray(Integer[] array){
        Integer[] nonZeroArray = (Integer[]) Arrays.stream(array).filter(i -> i!=0).collect(Collectors.toList()).toArray();
        return nonZeroArray;
    }
}
