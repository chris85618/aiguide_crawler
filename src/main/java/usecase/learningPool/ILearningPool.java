package usecase.learningPool;

import usecase.learningPool.learningResult.dto.LearningResultDTO;
import usecase.learningPool.learningTask.dto.LearningTaskDTO;

public interface ILearningPool {
    void startLearningPool();
    void stopLearningPool();
    void  enQueueLearningTaskDTO(LearningTaskDTO learningTaskDTO);
    boolean isLearningTaskDTOQueueEmpty();
    LearningResultDTO deQueueLearningResultDTO();
    boolean isLearningResultDTOQueueEmpty();
}
