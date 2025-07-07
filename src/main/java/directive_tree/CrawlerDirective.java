package directive_tree;

import util.HighLevelAction;

import java.util.List;

public class CrawlerDirective {

    private final String stateId;
    private final String dom;
    private final List<HighLevelAction> highLevelActions;
    private final boolean isDuplicatedTest;

    public CrawlerDirective(String stateId, String dom, List<HighLevelAction> highLevelActions, boolean isDuplicatedTest) {
        this.stateId = stateId;
        this.dom = dom;
        this.highLevelActions = highLevelActions;
        this.isDuplicatedTest = isDuplicatedTest;
    }

    public String getStateId() {
        return stateId;
    }

    public String getDom() {
        return dom;
    }

    public List<HighLevelAction> getHighLevelActions() {
        return highLevelActions;
    }

    public Boolean isDuplicatedTest() {
        return isDuplicatedTest;
    }
}
