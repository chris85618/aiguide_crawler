package directive_tree;

import util.HighLevelAction;

import java.util.List;

import usecase.learningPool.formInputValueList.FormInputValueList;

public class CrawlerDirective {

    private final String stateId;
    private final String dom;
    private final List<HighLevelAction> highLevelActions;
    private final FormInputValueList formInputValueList;

    public CrawlerDirective(String stateId, String dom, List<HighLevelAction> highLevelActions, FormInputValueList formInputValueList) {
        this.stateId = stateId;
        this.dom = dom;
        this.highLevelActions = highLevelActions;
        this.formInputValueList = formInputValueList;
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

    public FormInputValueList getFormInputValueList() {
        return formInputValueList;
    }
}
