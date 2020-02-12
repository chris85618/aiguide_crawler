package directive_tree;

import learning_data.LearningResult;
import learning_data.LearningTask;
import util.Action;

import java.util.List;
import java.util.ArrayList;

public class Directive {
    private InputPage parentInputPage;
    private List<InputPage> inputPageList;
    private List<Action> actionSequence;

    public Directive(InputPage parentInputPage, List<Action> actionSequence) {
        this.parentInputPage = parentInputPage;
        this.actionSequence = actionSequence;
        this.inputPageList = new ArrayList<>();
    }

    public List<Action> getActionSequence() {
        return actionSequence;
    }

    public void addInputPage() {
        inputPageList.add(new InputPage());
    }

    public int getInputPageListSize() {
        return inputPageList.size();
    }

    public List<InputPage> getChild() {
        return new ArrayList<>();
    }

    public InputPage findChildByTask(LearningTask task) {
        return new InputPage();
    }

}
