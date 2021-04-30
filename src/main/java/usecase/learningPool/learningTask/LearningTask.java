package usecase.learningPool.learningTask;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningTask {
    private final List<List<util.Action>> actionSequence;
    private final Integer[] coverage;
    private final String targetURL;
    private final String stateID;
    private final Map<String, String> learningConfig;

    public LearningTask(List<List<util.Action>> actionSequence, String stateID) {
        this(actionSequence, new Integer[0], "", stateID, new HashMap<>());
    }

    public LearningTask(List<List<util.Action>> actionSequence, Integer[] coverage, String targetURL, String stateID, Map<String, String> learningConfig) {
        this.actionSequence = actionSequence;
        this.coverage = coverage;
        this.targetURL = targetURL;
        this.stateID = stateID;
        this.learningConfig = learningConfig;
    }

    public List<List<util.Action>> getActionSequence() {
        return actionSequence;
    }

    public Integer[] getCoverage() {
        return coverage;
    }

    public String getTargetURL() {
        return this.targetURL;
    }

    public String getStateID() {
        return stateID;
    }

    public Map<String, String> getLearningConfig() {
        return learningConfig;
    }

}
