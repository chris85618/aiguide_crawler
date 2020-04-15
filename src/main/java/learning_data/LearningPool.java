package learning_data;

import util.ActionFactory;
import util.HighLevelAction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LearningPool {
    private Queue<LearningTask> learningTasks;
    private Queue<LearningResult> learningResults;
    private Boolean stopLearning;

    public LearningPool() {
        this.learningTasks = new LinkedList<>();
        this.learningResults = new LinkedList<>();
        this.stopLearning = false;
    }

    public synchronized void addTask(LearningTask task) {
        boolean succ;
        succ = learningTasks.offer(task);
        assert succ : "add LearningTask fail";
    }

    public synchronized void addResult(LearningResult result) {
        boolean succ;
        succ = learningResults.offer(result);
        assert succ : "add LearningResult fail";
    }

    public synchronized void addResultByData(List<HighLevelAction> actionSequence, String taskID, boolean isDone) {
        boolean succ;
        System.out.println(taskID);
        succ = learningResults.offer(new LearningResult(actionSequence, taskID, isDone));
        assert succ : "add LearningResult fail";
    }

    public synchronized LearningTask takeTask() {
        return learningTasks.poll();
    }

    public synchronized LearningResult takeResult() {
        return learningResults.poll();
    }

    public synchronized List<LearningResult> takeResults() {
        List<LearningResult> results = new ArrayList<>();
        while(!learningResults.isEmpty()){
            results.add(learningResults.poll());
        }
        return results;
    }

    public synchronized int getTaskSize() {
        return learningTasks.size();
    }

    public synchronized int getResultSize() { return learningResults.size(); }

    public synchronized void setStopLearning() { stopLearning = true; }

    public synchronized Boolean getStopLearning() { return stopLearning; }

    public synchronized ActionFactory getActionFactory() { return new ActionFactory(); }
}
