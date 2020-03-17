package util;

import java.util.List;

public class ActionFactory {
    public Action createAction(String xpath, String value) { return new Action(xpath, value); }
    public HighLevelAction createHighLevelAction(List<Action> actions) { return new HighLevelAction(actions); }
}
