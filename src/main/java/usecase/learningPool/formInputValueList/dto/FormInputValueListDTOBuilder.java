package usecase.learningPool.formInputValueList.dto;

import java.util.ArrayList;
import java.util.List;

public class FormInputValueListDTOBuilder {
    private List<FormInputValueDTO> formInputValueDTOList;

    public FormInputValueListDTOBuilder() {
        this.formInputValueDTOList = new ArrayList<>();
    }

    public void addFormInputValue(FormInputValueDTO formInputValue) {
        this.formInputValueDTOList.add(formInputValue);
    }

    public FormInputValueListDTO build() {
        return new FormInputValueListDTO(this.formInputValueDTOList);
    }
}
