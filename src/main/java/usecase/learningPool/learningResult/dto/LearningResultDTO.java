package usecase.learningPool.learningResult.dto;

import usecase.learningPool.action.dto.HighLevelActionDTO;
import util.HighLevelAction;

import java.util.List;

public class LearningResultDTO {
    private final HighLevelActionDTO[] highLevelActionDTOList;
    private final String taskID;
    private final String formXPath;
    private final boolean[] codeCoverageVector;
    private final boolean[] originalCodeCoverageVector;
    private final boolean isDone;
    private final boolean isDuplicatedTest;

    public LearningResultDTO(List<HighLevelActionDTO> highLevelActionDTOList, String taskID, String formXpath, boolean[] codeCoverageVector, boolean[] originalCodeCoverageVector, boolean isDone) {
        this(highLevelActionDTOList.toArray(HighLevelActionDTO[]::new), taskID, formXpath, codeCoverageVector, originalCodeCoverageVector, false, isDone);
    }

    public LearningResultDTO(List<HighLevelActionDTO> highLevelActionDTOList, String taskID, String formXpath, boolean[] codeCoverageVector, boolean[] originalCodeCoverageVector, boolean isDuplicatedTest, boolean isDone) {
        this(highLevelActionDTOList.toArray(HighLevelActionDTO[]::new), taskID, formXpath, codeCoverageVector, originalCodeCoverageVector, isDuplicatedTest, isDone);
    }

    public LearningResultDTO(HighLevelActionDTO[] highLevelActionDTOList, String taskID, String formXpath, boolean[] codeCoverageVector, boolean[] originalCodeCoverageVector, boolean isDone) {
        this(highLevelActionDTOList, taskID, formXpath, codeCoverageVector, originalCodeCoverageVector, false, isDone);
    }

    public LearningResultDTO(HighLevelActionDTO[] highLevelActionDTOList, String taskID, String formXpath, boolean[] codeCoverageVector, boolean[] originalCodeCoverageVector, boolean isDuplicatedTest, boolean isDone) {
        this.highLevelActionDTOList = highLevelActionDTOList;
        this.taskID = taskID;
        this.formXPath = formXpath;
        this.codeCoverageVector = codeCoverageVector;
        this.originalCodeCoverageVector = originalCodeCoverageVector;
        this.isDone = isDone;
        this.isDuplicatedTest = isDuplicatedTest;
    }

    public HighLevelActionDTO[] getHighLevelActionDTOList() {
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

    public boolean isDuplicatedTest() {
        return this.isDuplicatedTest;
    }

    public boolean isDone() {
        return isDone;
    }
}