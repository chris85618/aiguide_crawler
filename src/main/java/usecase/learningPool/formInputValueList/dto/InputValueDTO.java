package usecase.learningPool.formInputValueList.dto;

public class InputValueDTO {
    private String xpath;
    private int action;
    private String inputValue;

    public InputValueDTO(final String xpath, final int action, final String inputValue) {
        this.xpath = xpath;
        this.action = action;
        this.inputValue = inputValue;
    }

    public String getXpath() {
        return xpath;
    }

    public String getInputValue() {
        return inputValue;
    }

    public int getAction() {
        return action;
    }
}
