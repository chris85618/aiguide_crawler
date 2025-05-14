package usecase.learningPool.formInputValueList.dto;

import java.util.ArrayList;
import java.util.List;

public class FormInputValueDTOBuilder {
    private List<InputValueDTO> inputValueDTO;

    public FormInputValueDTOBuilder() {
        this.inputValueDTO = new ArrayList<>();
    }

    public void addInputValue(InputValueDTO inputValue) {
        this.inputValueDTO.add(inputValue);
    }

    public FormInputValueDTO build() {
        return new FormInputValueDTO(this.inputValueDTO);
    }
}
