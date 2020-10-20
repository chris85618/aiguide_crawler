package server_instance.codeCoverage;

import util.JsonFileParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IstanbulCodeCoverageCollector implements CodeCoverageCollector {
    private String url;
    private Integer[] totalBranchCoverage = null;
    private Integer[] totalStatementCoverage = null;

    public IstanbulCodeCoverageCollector(int port) {
        this.url = "http://localhost:" + port;
    }

    public Integer[] getBranchCoverageVector() {
        JsonFileParser parser = new JsonFileParser(this.url + "/coverage/object");
        List<Integer> coverage_vector = new ArrayList<>();
        for(String key: parser.getAllKeys()) {
            Map<String, List<String>> map = parser.getJsonFileValuesAsArray(key, "b");
            for(Map.Entry<String, List<String>> entry: map.entrySet()){
                for(String str: entry.getValue()) {
                    coverage_vector.add(Integer.parseInt(str));
                }
            }
        }
        return coverage_vector.toArray(new Integer[0]);
    }

    public Integer[] getStatementCoverageVector() {
        JsonFileParser parser = new JsonFileParser(this.url + "/coverage/object");
        List<Integer> coverage_vector = new ArrayList<>();
        for(String key: parser.getAllKeys()) {
            Map<String, String> map = parser.getJsonFileValues(key, "s");
            for(Map.Entry<String, String> entry: map.entrySet()){
                coverage_vector.add(Integer.parseInt(entry.getValue()));
            }
        }
        return coverage_vector.toArray(new Integer[0]);
    }

    public void resetCoverage() {
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url + "/coverage/reset").openConnection();
            http.setRequestMethod("GET");
            InputStream input = http.getInputStream();
            byte[] data = new byte[1024];
            int idx = input.read(data);
            String str = new String(data, 0, idx);
            System.out.println("Reset coverage: " + str);
            input.close();
            http.disconnect();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Reset coverage error!!");
        }
    }

    public void recordCoverage() {
        if(totalBranchCoverage == null || totalStatementCoverage == null) {
            totalStatementCoverage = getStatementCoverageVector();
            totalBranchCoverage = getBranchCoverageVector();
        }
        else {
            Integer[] currentStatementCoverage = getStatementCoverageVector();
            for (int i = 0; i < totalStatementCoverage.length; i++) {
                if (currentStatementCoverage[i] != 0) {
                    totalStatementCoverage[i] = currentStatementCoverage[i];
                }
            }

            Integer[] currentBranchCoverage = getBranchCoverageVector();
            for (int i = 0; i < totalBranchCoverage.length; i++) {
                if (currentBranchCoverage[i] != 0) {
                    totalBranchCoverage[i] = currentBranchCoverage[i];
                }
            }
        }
    }

    public Integer[] getTotalBranchCoverageVector() {
        return totalBranchCoverage.clone();
    }

    public Integer[] getTotalStatementCoverageVector() {
        return totalStatementCoverage.clone();
    }

    public void resetTotalCoverage() {
        totalBranchCoverage = null;
        totalStatementCoverage = null;
    }
}
