package usecase.learningPool.learningResult.dto;

import usecase.learningPool.action.dto.HighLevelActionDTO;

import java.util.ArrayList;
import java.util.List;

public class LearningResultDTOBuilder {
    private List<HighLevelActionDTO> highLevelActionDTOList;
    private String taskID;
    private boolean[] codeCoverageVector;
    private boolean[] originalCodeCoverageVector;
    private boolean isDone;

    public LearningResultDTOBuilder(){}

    public void setHighLevelActionDTOList(List<HighLevelActionDTO> highLevelActionDTOList) {
        this.highLevelActionDTOList = highLevelActionDTOList;
    }

    public void setHighLevelActionDTOList() {
        this.highLevelActionDTOList = new ArrayList<>();
    }

    public void appendHighLevelActionDTOList(HighLevelActionDTO highLevelActionDTO){
        highLevelActionDTOList.add(highLevelActionDTO);
    }

    public void setTaskID(String taskID){
        this.taskID = taskID;
    }

    public void setCodeCoverageVector(boolean[] codeCoverageVector){
        this.codeCoverageVector = codeCoverageVector;
    }

    public void setOriginalCodeCoverageVector(boolean[] originalCodeCoverageVector) {
        this.originalCodeCoverageVector = originalCodeCoverageVector;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public LearningResultDTO build(){
        return new LearningResultDTO(highLevelActionDTOList, taskID, codeCoverageVector, originalCodeCoverageVector, isDone);
    }
}
