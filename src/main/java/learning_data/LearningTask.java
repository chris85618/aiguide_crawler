package learning_data;


import ntut.edu.aiguide.crawljax.plugins.domain.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningTask {
    private final List<Action> actionSequence;
    private final int[] coverage;
    private final String rootURL;
    private final Integer stateID;
    private final Map<String, String> learningConfig;

    public LearningTask(List<Action> actionSequence, Integer stateID) {
        this(actionSequence, new int[0], "", stateID, new HashMap<String, String>());
    }

    public LearningTask(List<Action> actionSequence, int[] coverage, String rootURL, Integer stateID, Map<String, String> learningConfig) {
        this.actionSequence = actionSequence;
        this.coverage = coverage;
        this.rootURL = rootURL;
        this.stateID = stateID;
        this.learningConfig = learningConfig;
    }

    public List<Action> getActionSequence() {
        return actionSequence;
    }

    public int[] getCoverage() {
        return coverage;
    }

    public String getRootURL() {
        return this.rootURL;
    }

    public Integer getStateID() {
        return stateID;
    }

    public Map<String, String> getLearningConfig() {
        return learningConfig;
    }

}
