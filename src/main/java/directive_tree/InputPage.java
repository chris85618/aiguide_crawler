package directive_tree;

import java.util.ArrayList;
import java.util.List;

public class InputPage {
    private final Directive parentDirective;
    private final String stateID;
    private final String targetURL;
    private final List<Directive> directiveList;
    private Boolean isDone;

    public InputPage(Directive parentDirective, String stateID, String targetURL) {
        this.parentDirective = parentDirective;
        this.stateID = stateID;
        this.targetURL = targetURL;
        this.directiveList = new ArrayList<>();
        this.isDone = false;
    }

    public void addDirective(Directive directive) { directiveList.add(directive); }

    public Directive getParent() {
        return parentDirective;
    }

    public List<Directive> getChild() {
        return directiveList;
    }

    public Boolean isDone() {
        return isDone;
    }

    public void setDone() {
        isDone = true;
    }

    public String getStateID() {
        return stateID;
    }

    public String getTargetURL() { return targetURL; }

    public Boolean compareStateID(String stateID) {
        return this.stateID.equals(stateID);
    }
}
