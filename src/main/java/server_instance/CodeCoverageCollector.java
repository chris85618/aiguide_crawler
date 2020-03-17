package server_instance;

import util.JsonFileParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CodeCoverageCollector {
    private String url;

    CodeCoverageCollector(int port) {
        this.url = "http://127.0.0.1:" + port;
    }

    Integer[] getCoverageVector() {
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

    void resetCoverage() {
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
}
