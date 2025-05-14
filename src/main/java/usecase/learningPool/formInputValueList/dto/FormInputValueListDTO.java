package usecase.learningPool.formInputValueList.dto;

import java.util.List;


public class FormInputValueListDTO {
    private List<FormInputValueDTO> formInputValueDTOList;

    public FormInputValueListDTO(List<FormInputValueDTO> formInputValueDTOList) {
        this.formInputValueDTOList = formInputValueDTOList;
    }

    public List<FormInputValueDTO> getFormInputValueList() {
        return this.formInputValueDTOList;
    }
}
