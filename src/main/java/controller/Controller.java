package controller;

import crawler.Crawler;
import crawler.Crawljax;
import directive_tree.DirectiveTreeHelper;
import learning_data.LearningPool;
import learning_data.LearningResult;
import learning_data.LearningTask;
import server_instance.ServerInstanceManagement;
import server_instance.TimeOffManagementServer;
import util.Action;
import util.Config;
import util.GatewayHelper;
import util.HighLevelAction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Controller {
    private DirectiveTreeHelper DT;
    private Config config;
    private ServerInstanceManagement serverInstance;
    private Crawler crawler;
    private Map<String, Boolean> taskCompleteMap;
    private LearningPool learningPool;
    private GatewayHelper gatewayHelper;
    private int task_counter = 0;

    public Controller(Config config) {
        this.config = config;
        this.serverInstance = new TimeOffManagementServer(this.config.AUT_PORT);
        this.crawler = new Crawljax(serverInstance);
        this.DT = new DirectiveTreeHelper();
        this.taskCompleteMap = new TreeMap<>();
        this.learningPool = new LearningPool();
        this.gatewayHelper = new GatewayHelper(this.config.SERVER_IP, this.config.AGENTS, this.learningPool);
    }

    public void execute() throws InterruptedException {
        boolean isDone = false;
        gatewayHelper.startGateway();
        serverInstance.createServerInstance();
        while(!isDone){
            while(!DT.isTreeComplete()){
                LinkedHashMap<String, List<HighLevelAction>> crawlerDirectives = DT.takeFirstUnprocessedCrawlerDirectives();
                List<LearningTask> learningTaskList = crawler.crawlingWithDirectives(config, crawlerDirectives);
                for(LearningTask task: learningTaskList){
                    if(taskCompleteMap.get(task.getStateID()) == null){
                        task_counter++;
                        taskCompleteMap.put(task.getStateID(), false);
                        learningPool.addTask(task);
                        DT.addInputPage(task);
                        System.out.println("TaskCompleteMap when add InputPage:" + taskCompleteMap);
                    }
                }
                System.out.println("Already have " + task_counter + " tasks.");
            }
            isDone = checkCrawlingDone();
            if(!isDone){
                List<LearningResult> results;
                results = waitAndGetLearningResults();
                DT.addDirectives(results);
            }
            DT.printDirectiveTree();
            DT.drawDirectiveTree();
        }
        learningPool.setStopLearning();
        crawler.generateGraph();
        serverInstance.closeServerInstance();
        gatewayHelper.closeGateway(config.SLEEP_TIME);
    }

    private boolean checkCrawlingDone() {
        boolean isDone = true;
        for(Map.Entry<String, Boolean> entry: taskCompleteMap.entrySet()){
            if(!entry.getValue()){
                isDone = false;
                break;
            }
        }
        if(learningPool.getTaskSize() != 0) isDone = false;
        if(learningPool.getResultSize() != 0) isDone = false;
        return isDone;
    }

    private List<LearningResult> waitAndGetLearningResults() {
        List<LearningResult> results;
        while (true){
            results = learningPool.takeResults();
            if(!results.isEmpty()){
                checkResultIsDone(results);
                return results;
            }
            try {
                Thread.sleep(config.SLEEP_TIME * 1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    private void checkResultIsDone(List<LearningResult> results) {
        for(LearningResult result: results){
            System.out.println("===================== Learning Result =====================");
            System.out.println("Learning Result:");
            System.out.println(result.getTaskID());
            if(result.getActionSequence() != null){
                System.out.print("Action Sequence:\n[");
                for(HighLevelAction ha: result.getActionSequence()){
                    System.out.print("[");
                    for(Action a: ha.getActionSequence()){
                        System.out.print("('" + a.getXpath() + "', '" + a.getValue() + "')");
                    }
                    System.out.print("], ");
                }
                System.out.println("]");
            }
            System.out.println(result.isDone());
            System.out.println("===========================================================");
            if(result.isDone()) taskCompleteMap.put(result.getTaskID(), true);
            System.out.println("TaskCompleteMap when get LearningResult:" + taskCompleteMap);
        }
    }

}
