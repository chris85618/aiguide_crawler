package directive_tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class GraphDrawer {

    public JsonObject convertDirectviceToJson(Directive directive) {
        JsonObject directiveJson = new JsonObject();
        directiveJson.addProperty("Type","Directive");
        directiveJson.addProperty("id", directive.getID());

        JsonArray directiveChildren = new JsonArray();
        Iterator<InputPage> inputPageIterator = directive.getChild().iterator();
        while(inputPageIterator.hasNext()){
            directiveChildren.add(this.convertInputPageToJson(inputPageIterator.next()));
        }

        directiveJson.add("children", directiveChildren);

        return directiveJson;
    }

    public JsonObject convertInputPageToJson(InputPage inputPage) {
        JsonObject inputPageJson = new JsonObject();
        inputPageJson.addProperty("Type","InputPage");
        inputPageJson.addProperty("stateID", inputPage.getStateID());

        String targetURL = inputPage.getTargetURL();
//        inputPageJson.addProperty("targetURL",targetURL.substring(targetURL.indexOf("/", "https://".length()),targetURL.length()));
        inputPageJson.addProperty("targetURL",targetURL);


        JsonArray inputPageChildren = new JsonArray();
        Iterator<Directive> directiveIterator = inputPage.getChild().iterator();
        while(directiveIterator.hasNext()){
            inputPageChildren.add(this.convertDirectviceToJson(directiveIterator.next()));
        }

        inputPageJson.add("children", inputPageChildren);

        return inputPageJson;
    }

//    public void printAllNode(Directive directive) {
//        System.out.println(directive.toString());
//        for (InputPage ip : directive.getChild()) {
//            System.out.println(ip.toString());
//            for (Directive d : ip.getChild())
//                printAllNode(d);
//        }
//    }

    public void generateGraph(JsonObject directiveTreeJson, String fileName) {
        Graph DTGraph = new Graph();
        DTGraph.setTreeStructure(directiveTreeJson);
        DTGraph.saveFile(fileName, "./lib/directive_tree_drawer/d3jsFomat.html");
    }

    public void draw(Directive directiveRoot){
        JsonObject DTRootJson = this.convertDirectviceToJson(directiveRoot);
        String fileName = new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()) + ".html";
        this.generateGraph(DTRootJson, "./directiveTreeGraphic/" + fileName);
    }
}