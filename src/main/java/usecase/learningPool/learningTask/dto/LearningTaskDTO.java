package usecase.learningPool.learningTask.dto;

import org.apache.velocity.runtime.directive.Scope;
import usecase.learningPool.action.dto.HighLevelActionDTO;
import usecase.learningPool.learningResult.dto.LearningResultDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LearningTaskDTO {
    private final List<HighLevelActionDTO> highLevelActionDTOList;
    private final boolean[] codeCoverageVector;
    private final String targetURL;
    private final String stateID;
    private final Map<String, String> learningConfig;

    public LearningTaskDTO(List<HighLevelActionDTO> highLevelActionDTOList,
                           boolean[] codeCoverageVector,
                           String targetURL,
                           String stateID,
                           Map<String, String> learningConfig) {
        this.highLevelActionDTOList = highLevelActionDTOList;
        this.codeCoverageVector = codeCoverageVector;
        this.targetURL = targetURL;
        this.stateID = stateID;
        this.learningConfig = learningConfig;
    }

    public LearningTaskDTO(List<HighLevelActionDTO> highLevelActionDTOList, boolean[] codeCoverageVector, String targetURL, String stateID){
        this(highLevelActionDTOList, codeCoverageVector, targetURL, stateID, new LinkedHashMap<>());
    }

    public List<HighLevelActionDTO> getHighLevelActionDTOList() {
        return highLevelActionDTOList;
    }

    public boolean[] getCodeCoverageVector() {
        return codeCoverageVector;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public String getStateID() {
        return stateID;
    }

    public Map<String, String> getLearningConfig() {
        return learningConfig;
    }
}
