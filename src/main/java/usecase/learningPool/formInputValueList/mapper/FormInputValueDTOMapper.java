package usecase.learningPool.formInputValueList.mapper;

import usecase.learningPool.formInputValueList.FormInputValue;
import usecase.learningPool.formInputValueList.InputValue;
import usecase.learningPool.formInputValueList.dto.FormInputValueDTO;
import usecase.learningPool.formInputValueList.dto.InputValueDTO;


public class FormInputValueDTOMapper {
    public static FormInputValue mappingFormInputValueFrom(FormInputValueDTO formInputValueDTO){
        FormInputValue formInputValue = new FormInputValue();
        for (InputValueDTO inputValueDTO : formInputValueDTO.getInputValueList()) {
            InputValue inputValue = InputValueDTOMapper.mappingInputValueFrom(inputValueDTO);
            final String xpath = inputValue.getXpath();
            formInputValue.addInputValue(xpath, inputValue);
        }
        return formInputValue;
    }
}
