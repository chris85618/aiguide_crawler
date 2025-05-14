package usecase.learningPool.learningResult.mapper;

import usecase.learningPool.action.dto.HighLevelActionDTO;
import usecase.learningPool.action.mapper.HighLevelActionDTOMapper;
import usecase.learningPool.formInputValueList.FormInputValueList;
import usecase.learningPool.formInputValueList.dto.FormInputValueListDTO;
import usecase.learningPool.formInputValueList.mapper.FormInputValueListDTOMapper;
import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningResult.dto.LearningResultDTO;
import util.HighLevelAction;

import java.util.ArrayList;
import java.util.List;

public class LearningResultDTOMapper {
    public static LearningResult mappingLearningResultFrom(LearningResultDTO learningResultDTO){
        int actionAmount = 0;
        // Handle HighLevelAction
        List<HighLevelAction> highLevelActionList = new ArrayList<>();
        for (HighLevelActionDTO highLevelActionDTO: learningResultDTO.getHighLevelActionDTOList()){
            highLevelActionList.add(HighLevelActionDTOMapper.mappingHighLevelActionFrom(highLevelActionDTO));
            actionAmount += highLevelActionDTO.getActionDTOList().size();
        }

        // Handle Code Coverage
        int codeCoverageImprove = 0;
        boolean[] codeCoverageVector = learningResultDTO.getCodeCoverageVector();
        boolean[] originalCodeCoverageVector = learningResultDTO.getOriginalCodeCoverageVector();
        for (int i=0; i< codeCoverageVector.length; i++){
            if (!originalCodeCoverageVector[i] && codeCoverageVector[i]){
                codeCoverageImprove++;
            }
        }
        // Handle FormInputValueList
        final FormInputValueListDTO formInputValueListDTO = learningResultDTO.getFormInputValueListDTO();
        final FormInputValueList formInputValueList = FormInputValueListDTOMapper.mappingFormInputValueListFrom(formInputValueListDTO);

        return new LearningResult(highLevelActionList, learningResultDTO.getTaskID(), learningResultDTO.getFormXPath(), codeCoverageImprove, actionAmount, formInputValueList, learningResultDTO.isDone());
    }
}
