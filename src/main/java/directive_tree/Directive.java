package directive_tree;

import util.Action;

import java.util.List;
import java.util.ArrayList;

public class Directive {
    private List<InputPage> inputPageList;
    private final InputPage parentInputPage;
    private final List<Action> actionSequence;

    public Directive(InputPage parentInputPage, List<Action> actionSequence) {
        this.parentInputPage = parentInputPage;
        this.actionSequence = actionSequence;
        this.inputPageList = new ArrayList<>();
    }

    public List<Action> getActionSequence() {
        return actionSequence;
    }

    public void addInputPage(InputPage ip) {
        inputPageList.add(ip);
    }

    public InputPage getParent() {
        return parentInputPage;
    }

    public List<InputPage> getChild() {
        return inputPageList;
    }

    public InputPage findInputPageByStateID(String stateID) {
        for (InputPage ip : inputPageList) {
            if (ip.compareStateID(stateID))
                return ip;
            for (Directive d : ip.getChild()){
                InputPage page = d.findInputPageByStateID(stateID);
                if (page != null) return page;
            }
        }
        return null;
    }

}
