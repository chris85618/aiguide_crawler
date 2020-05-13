package directive_tree;

import com.google.gson.JsonObject;

import java.io.*;

public class Graph {
    private JsonObject directiveTreeJson;

    public JsonObject getDirectiveTreeJson() {
        return directiveTreeJson;
    }

    public void setTreeStructure(JsonObject directiveTreeJson) {
        this.directiveTreeJson = directiveTreeJson;
    }

    public void saveFile(String filePath, String graphFormatPath) {
        String graphFormat = this.getFileString(graphFormatPath);
        String graph = String.format(graphFormat, this.directiveTreeJson.toString());
        this.writeFile(filePath, graph);
    }

    public String getFileString(String filePath) {
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
}
