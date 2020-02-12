package learning_data;

import java.util.LinkedList;
import java.util.Queue;

// ToDo: Must add synchronous code.
public class LearningPool {
    private Queue<LearningTask> learningTasks;
    private Queue<LearningResult> learningResults;

    public LearningPool() {
        this.learningTasks = new LinkedList<>();
        this.learningResults = new LinkedList<>();
    }

    public void addTask(LearningTask task) {
        boolean succ;
        succ = learningTasks.offer(task);
        assert succ : "add LearningTask fail";
    }

    public void addResult(LearningResult result) {
        boolean succ;
        succ = learningResults.offer(result);
        assert succ : "add LearningResult fail";
    }

    public LearningTask takeTask() {
        return learningTasks.poll();
    }

    public LearningResult takeResult() {
        return learningResults.poll();
    }

    public int getTaskSize() {
        return learningTasks.size();
    }

    public int getResultSize() {
        return learningResults.size();
    }
}
