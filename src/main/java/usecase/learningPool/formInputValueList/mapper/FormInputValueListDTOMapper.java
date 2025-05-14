package usecase.learningPool.formInputValueList.mapper;

import usecase.learningPool.formInputValueList.FormInputValue;
import usecase.learningPool.formInputValueList.FormInputValueList;
import usecase.learningPool.formInputValueList.dto.FormInputValueDTO;
import usecase.learningPool.formInputValueList.dto.FormInputValueListDTO;


public class FormInputValueListDTOMapper {
    public static FormInputValueList mappingFormInputValueListFrom(FormInputValueListDTO formInputValueListDTO){
        FormInputValueList formInputValueList = new FormInputValueList();
        for (FormInputValueDTO formInputValueDTO : formInputValueListDTO.getFormInputValueList()) {
            FormInputValue formInputValue = FormInputValueDTOMapper.mappingFormInputValueFrom(formInputValueDTO);
            formInputValueList.addFormInputValue(formInputValue);
        }
        return formInputValueList;
    }
}
