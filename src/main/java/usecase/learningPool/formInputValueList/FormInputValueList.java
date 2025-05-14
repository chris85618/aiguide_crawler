package usecase.learningPool.formInputValueList;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class FormInputValueList {
    private List<FormInputValue> formInputValueList;


    public FormInputValueList() {
        this.formInputValueList = new ArrayList<>();
    }

    public FormInputValueList(final List<FormInputValue> formInputValueList) {
        this.formInputValueList = formInputValueList;
    }

    public void addFormInputValue(final FormInputValue formInputValue) {
        this.formInputValueList.add(formInputValue);
    }
    public FormInputValue getFormInputValue(final int index) {
        return this.formInputValueList.get(index);
    }

    public List<FormInputValue> getAllFormInputValues() {
        return this.formInputValueList;
    }

    public Iterator<FormInputValue> iterator() {
        return this.formInputValueList.iterator();
    }

    public int size() {
        return this.formInputValueList.size();
    }
}
