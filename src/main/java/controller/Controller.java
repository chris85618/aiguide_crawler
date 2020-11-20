package controller;

import crawler.Crawler;
import crawler.Crawljax;
import directive_tree.DirectiveTreeHelper;
import learning_data.LearningPool;
import learning_data.LearningResult;
import learning_data.LearningTask;
import server_instance.NodeBBServer;
import server_instance.ServerInstanceManagement;
import server_instance.TimeOffManagementServer;
import server_instance.codeCoverage.CodeCoverage;
import util.Config;
import util.GatewayHelper;
import util.HighLevelAction;
import util.LogHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Controller {
    private DirectiveTreeHelper directiveTreeHelper;
    private Config config;
    private ServerInstanceManagement serverInstance;
    private Crawler crawler;
    private Map<String, Boolean> taskCompleteMap;
    private LearningPool learningPool;
    private GatewayHelper gatewayHelper;

    public Controller(Config config) {
        this.config = config;
        this.serverInstance = createServerInstanceManagement();
        this.crawler = new Crawljax(serverInstance);
        this.directiveTreeHelper = new DirectiveTreeHelper();
        this.taskCompleteMap = new TreeMap<>();
        this.learningPool = new LearningPool();
        this.gatewayHelper = new GatewayHelper(this.config.SERVER_IP, this.config.AGENTS, this.learningPool);
    }

    private ServerInstanceManagement createServerInstanceManagement() {
        if(config.AUT_NAME.equals("timeoff-management")) return new TimeOffManagementServer(this.config.AUT_NAME, this.config.AUT_PORT);
        else if(config.AUT_NAME.equals("nodebb")) return new NodeBBServer(this.config.AUT_NAME, this.config.AUT_PORT);

        throw new RuntimeException("AUT not fount when create server instance.");
    }

    public void execute() throws InterruptedException {
        boolean isDone = false;
        gatewayHelper.startGateway();
        serverInstance.createServerInstance();
        while(!isDone){
            while(!directiveTreeHelper.isTreeComplete()){
                LinkedHashMap<String, List<HighLevelAction>> crawlerDirectives = directiveTreeHelper.takeFirstUnprocessedCrawlerDirectives();
                List<LearningTask> learningTaskList = crawler.crawlingWithDirectives(config, crawlerDirectives);
                for(LearningTask task: learningTaskList){
                    if(taskCompleteMap.get(task.getStateID()) == null){
                        taskCompleteMap.put(task.getStateID(), false);
                        learningPool.addTask(task);
                        directiveTreeHelper.addInputPage(task);
                    }
                }
                LogHelper.writeAllLog();
            }
            isDone = checkCrawlingDone();
            if(!isDone){
                List<LearningResult> results;
                results = waitAndGetLearningResults();
                directiveTreeHelper.addDirectives(results);
            }
            directiveTreeHelper.writeDirectiveTree();
            directiveTreeHelper.drawDirectiveTree();

            CodeCoverage statementCoverage = serverInstance.getTotalCoverage().get("statement");
            CodeCoverage branchCoverage = serverInstance.getTotalCoverage().get("branch");

            LogHelper.summary("The statement coverage is: " + statementCoverage.getCoveredAmount() + String.format("(%.2f%%)", 100 * statementCoverage.getPercent()));
            LogHelper.summary("The branch coverage is: " + branchCoverage.getCoveredAmount() + String.format("(%.2f%%)", 100 * branchCoverage.getPercent()));

            LogHelper.summary("Total task is: " + taskCompleteMap.size() + " , Remain task is: " + learningPool.getTaskSize());

            LogHelper.writeAllLog();
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
        for(LearningResult result: results)
            if(result.isDone()) taskCompleteMap.put(result.getTaskID(), true);
    }

}
