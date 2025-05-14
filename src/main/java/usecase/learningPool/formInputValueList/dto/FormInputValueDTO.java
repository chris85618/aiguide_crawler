package usecase.learningPool.formInputValueList.dto;

import java.util.List;

public class FormInputValueDTO {
    private List<InputValueDTO> inputValueList;

    public FormInputValueDTO(List<InputValueDTO> inputValueList) {
        this.inputValueList = inputValueList;
    }

    public List<InputValueDTO> getInputValueList() {
        return this.inputValueList;
    }
}
