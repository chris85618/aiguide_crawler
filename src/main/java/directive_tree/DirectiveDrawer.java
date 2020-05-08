package directive_tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Iterator;

public class DirectiveDrawer {

    public JsonObject convertDirectviceToJson(Directive directive) {
        JsonObject directiveJson = new JsonObject();
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
        inputPageJson.addProperty("stateID", inputPage.getStateID());
        inputPageJson.addProperty("targetURL",inputPage.getTargetURL());

        JsonArray inputPageChildren = new JsonArray();
        Iterator<Directive> directiveIterator = inputPage.getChild().iterator();
        while(directiveIterator.hasNext()){
            inputPageChildren.add(this.convertDirectviceToJson(directiveIterator.next()));
        }

        inputPageJson.add("children", inputPageChildren);

        return inputPageJson;
    }

    public void printAllNode(Directive directive) {
        System.out.println(directive.toString());
        for (InputPage ip : directive.getChild()) {
            System.out.println(ip.toString());
            for (Directive d : ip.getChild())
                printAllNode(d);
        }
    }
}
