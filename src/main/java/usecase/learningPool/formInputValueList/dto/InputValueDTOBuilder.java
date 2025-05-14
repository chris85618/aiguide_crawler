package usecase.learningPool.formInputValueList.dto;

public class InputValueDTOBuilder {
    private String xpath;
    private int action;
    private String inputValue;

    public void setXpath(final String xpath) {
        this.xpath = xpath;
    }

    public void setAction(final int action) {
        this.action = action;
    }

    public void setInputValue(final String inputValue) {
        this.inputValue = inputValue;
    }

    public InputValueDTO build() {
        return new InputValueDTO(xpath, action, inputValue);
    }
}
