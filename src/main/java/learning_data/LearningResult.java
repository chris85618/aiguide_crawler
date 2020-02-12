package learning_data;

import util.Action;

import java.util.List;

public class LearningResult {
    private final List<Action> actionSequence;
    private final boolean isDone;

    public LearningResult(List<Action> actionSequence, boolean isDone) {
        this.actionSequence = actionSequence;
        this.isDone = isDone;
    }

    public List<Action> getActionSequence() {
        return actionSequence;
    }

    public boolean isDone() {
        return isDone;
    }
}
