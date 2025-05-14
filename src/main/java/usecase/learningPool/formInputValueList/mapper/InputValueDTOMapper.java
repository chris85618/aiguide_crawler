package usecase.learningPool.formInputValueList.mapper;

import usecase.learningPool.formInputValueList.InputValue;
import usecase.learningPool.formInputValueList.dto.InputValueDTO;


public class InputValueDTOMapper {
    public static InputValue mappingInputValueFrom(InputValueDTO inputValueDTO){
        final String xpath = inputValueDTO.getXpath();
        final int action = inputValueDTO.getAction();
        final String inputValue = inputValueDTO.getInputValue();
        return new InputValue(xpath, action, inputValue);
    }
}
