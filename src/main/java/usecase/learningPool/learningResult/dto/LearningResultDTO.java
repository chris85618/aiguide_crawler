package usecase.learningPool.learningResult.dto;

import java.util.List;

import usecase.learningPool.action.dto.HighLevelActionDTO;
import usecase.learningPool.formInputValueList.dto.FormInputValueListDTO;

public class LearningResultDTO {
    private final List<HighLevelActionDTO> highLevelActionDTOList;
    private final String taskID;
    private final String formXPath;
    private final boolean[] codeCoverageVector;
    private final boolean[] originalCodeCoverageVector;
    private final FormInputValueListDTO formInputValueListDTO;
    private final boolean isDone;

    public LearningResultDTO(List<HighLevelActionDTO> highLevelActionDTOList, String taskID, String formXpath, boolean[] codeCoverageVector, boolean[] originalCodeCoverageVector, FormInputValueListDTO formInputValueListDTO, boolean isDone) {
        this.highLevelActionDTOList = highLevelActionDTOList;
        this.taskID = taskID;
        this.formXPath = formXpath;
        this.codeCoverageVector = codeCoverageVector;
        this.originalCodeCoverageVector = originalCodeCoverageVector;
        this.formInputValueListDTO = formInputValueListDTO;
        this.isDone = isDone;
    }

    public List<HighLevelActionDTO> getHighLevelActionDTOList() {
        return highLevelActionDTOList;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getFormXPath() {
        return formXPath;
    }

    public boolean[] getCodeCoverageVector() {
        return codeCoverageVector;
    }

    public boolean[] getOriginalCodeCoverageVector() {
        return originalCodeCoverageVector;
    }

    public FormInputValueListDTO getFormInputValueListDTO() {
        return this.formInputValueListDTO;
    }

    public boolean isDone() {
        return isDone;
    }
}