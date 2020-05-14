package directive_tree;

import com.google.gson.JsonObject;

import java.io.*;

public class Graph {
    private JsonObject directiveTreeJson;
    private String graphLibFileName = "d3.min.js";

    public JsonObject getDirectiveTreeJson() {
        return directiveTreeJson;
    }

    public void setTreeStructure(JsonObject directiveTreeJson) {
        this.directiveTreeJson = directiveTreeJson;
    }

    public void saveFile(String filePath, String graphFormatPath) {
        String graphFormat = this.getFileText(graphFormatPath);
        String graph = String.format(graphFormat, this.directiveTreeJson.toString());
        String graphLib = this.getFileText(this.getGraphLibPath(graphFormatPath));

        this.writeFile(this.getGraphLibPath(filePath),graphLib);
        this.writeFile(filePath, graph);
    }

    public String getFileText(String filePath) {
        String fileText = "";
        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fileLinetext;
            while((fileLinetext = br.readLine()) != null){
                fileText += fileLinetext + "\n";
            }
            fileText = fileText.substring(0,fileText.length()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileText;
    }

    public void writeFile(String filePath, String writeText) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(writeText);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPreviousLevePath(String filePath) {
        String previousLevePath = "./";
        int lastSlashIndex = getLastSlashIndex(filePath);

        if (lastSlashIndex > 1) previousLevePath = filePath.substring(0, lastSlashIndex);

        return previousLevePath;
    }

    private int getLastSlashIndex(String path){
        int lastSlashIndex = path.lastIndexOf("/");
        int lastBackslashIndex = path.lastIndexOf("\\");

        return -1 * lastSlashIndex * lastBackslashIndex;
    }

    private String getGraphLibPath(String graphPath){
        String graphLibPath = "";
        graphLibPath += this.getPreviousLevePath(graphPath);

        graphLibPath += graphPath.substring(graphPath.length()-1) == "/" ? this.graphLibFileName : "/" + this.graphLibFileName;

        return graphLibPath;
    }

}
