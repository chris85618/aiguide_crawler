package usecase.learningPool.formInputValueList;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FormInputValue {
    private Map<String, InputValue> inputValues;

    public FormInputValue() {
        this.inputValues = new HashMap<>();
    }

    public FormInputValue(final List<InputValue> inputValueList) {
        this.inputValues = new HashMap<>();
        for (InputValue inputValue : inputValueList) {
            final String xpath = inputValue.getXpath(); 
            this.inputValues.put(xpath, inputValue);
        }
    }

    public void addInputValue(final String xpath, final InputValue inputValue) {
        this.inputValues.put(xpath, inputValue);
    }

    public InputValue getInputValue(final String xpath) {
        return this.inputValues.get(xpath);
    }

    public Map<String, InputValue> getAllInputValues() {
        return this.inputValues;
    }
}
