package learning_data;

import util.HighLevelAction;

import java.util.List;

public class LearningResult {
    private final List<HighLevelAction> actionSequence;
    private final String taskID;
    private final boolean isDone;

    public LearningResult(List<HighLevelAction> actionSequence, String taskID, boolean isDone) {
        this.actionSequence = actionSequence;
        this.taskID = taskID;
        this.isDone = isDone;
    }

    public List<HighLevelAction> getActionSequence() {
        return actionSequence;
    }

    public String getTaskID() { return taskID; }

    public boolean isDone() {
        return isDone;
    }
}
