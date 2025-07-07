package usecase.learningPool.learningResult;

import util.HighLevelAction;

import java.util.List;

public class LearningResult {
    private final List<HighLevelAction> actionSequence;
    private final String taskID;
    private final String formXPath;
    private final int coverageImproved;
    private final int learningTargetActionSequenceLength;
    private final boolean isDone;
    private final boolean isDuplicatedTest;

    public LearningResult(List<HighLevelAction> actionSequence, String taskID, String formXPath, int coverageImproved, int learningTargetActionSequenceLength, boolean isDone) {
        this(actionSequence, taskID, formXPath, coverageImproved, learningTargetActionSequenceLength, false, isDone);
    }

    public LearningResult(List<HighLevelAction> actionSequence, String taskID, String formXPath, int coverageImproved, int learningTargetActionSequenceLength, boolean isDuplicatedTest, boolean isDone) {
        this.actionSequence = actionSequence;
        this.taskID = taskID;
        this.formXPath = formXPath;
        this.coverageImproved = coverageImproved;
        this.learningTargetActionSequenceLength = learningTargetActionSequenceLength;
        this.isDuplicatedTest = isDuplicatedTest;
        this.isDone = isDone;
    }

    public List<HighLevelAction> getActionSequence() {
        return actionSequence;
    }

    public String getTaskID() { return taskID; }

    public String getFormXPath() {
        return formXPath;
    }

    public int getCoverageImproved() {
        return coverageImproved;
    }

    public int getLearningTargetActionSequenceLength() {return learningTargetActionSequenceLength;}

    public boolean isDuplicatedTest() {
        return this.isDuplicatedTest;
    }

    public boolean isDone() {
        return isDone;
    }
}
