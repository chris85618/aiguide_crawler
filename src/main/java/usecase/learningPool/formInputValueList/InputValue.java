package usecase.learningPool.formInputValueList;

public class InputValue {
    
    private String xpath;
    private int action;
    private String inputValue;

    public InputValue(String xpath, int action, String inputValue) {
        this.xpath = xpath;
        this.action = action;
        this.inputValue = inputValue;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getValue() {
        return inputValue;
    }

    public void setValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
