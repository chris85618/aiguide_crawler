package adpater.controller;

import adpater.learningPool.Py4JLearningPool;
import crawler.Crawler;
import crawler.Crawljax;
import directive_tree.CrawlerDirective;
import directive_tree.Directive;
import directive_tree.DirectiveTreeHelper;
import learning_data.LearningPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server_instance.*;
import usecase.learningPool.ILearningPool;
import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningResult.dto.LearningResultDTO;
import usecase.learningPool.learningResult.mapper.LearningResultDTOMapper;
import usecase.learningPool.learningTask.LearningTask;
import server_instance.codeCoverage.CodeCoverage;
import usecase.learningPool.learningTask.mapper.LearningTaskDTOMapper;
import util.*;

import java.util.*;


public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private DirectiveTreeHelper directiveTreeHelper;
    private Config config;
    private ServerInstanceManagement serverInstance;
    private Crawler crawler;
    private Map<String, Boolean> taskCompleteMap;
    private List<String> inputPageUrls;
    private LearningPool learningPool;
    private ILearningPool learningPoolServer;

    public Controller(Config config) {
        this.config = config;
        this.serverInstance = createServerInstanceManagement();
        this.crawler = new Crawljax(serverInstance);
//        this.directiveTreeHelper = new DirectiveTreeHelper(createDefaultDirective());
        this.directiveTreeHelper = new DirectiveTreeHelper();
        this.taskCompleteMap = new TreeMap<>();
        this.inputPageUrls = new ArrayList<>();
        this.learningPool = new LearningPool();
        Map<String, String> agentConfig = this.config.AGENTS.get(0);
        this.learningPoolServer = new Py4JLearningPool(this.config.SERVER_IP,
                agentConfig.get("ip"),
                Integer.parseInt(agentConfig.get("java port")),
                Integer.parseInt(agentConfig.get("python port")));
    }

//    private Directive createDefaultDirective(){
//        List<HighLevelAction> highLevelActionList = new ArrayList<>();
//
//        List<Action> actionList = new ArrayList<>();
//
//        actionList = new ArrayList<>();
//        actionList.add(new Action("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/DIV[1]/INPUT[1]", "vector@selab.com"));
//        actionList.add(new Action("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/DIV[2]/INPUT[1]", "password"));
//        highLevelActionList.add(new HighLevelAction(actionList));
//
//        actionList = new ArrayList<>();
//        actionList.add(new Action("/HTML[1]/BODY[1]/DIV[1]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/FORM[1]/BUTTON[1]", ""));
//        highLevelActionList.add(new HighLevelAction(actionList));
//
//        return new Directive(null, "", highLevelActionList, 0, 0, true);
//    }

    private ServerInstanceManagement createServerInstanceManagement() {
        switch (config.AUT_NAME) {
            case "timeoff":
                return new TimeOffManagementServer(this.config.AUT_NAME, this.config.AUT_PORT);
            case "nodebb":
                return new NodeBBServer(this.config.AUT_NAME, this.config.AUT_PORT);
            case "keystoneJS":
                return new KeystoneJSServer(this.config.AUT_NAME, this.config.AUT_PORT);
            case "wagtails":
                return new WagtailsServer(this.config.AUT_NAME, this.config.AUT_PORT);
            case "django_blog_with_no_coverage":
                return new DjangoBlogServer(this.config.AUT_NAME, this.config.AUT_PORT);
            case "spring_petclinic_with_no_coverage":
                return new SpringPetclinicServer(this.config.AUT_NAME, this.config.AUT_PORT);
            case "kimai":
                return new KimaiServer(this.config.AUT_NAME, this.config.AUT_PORT);
        }

        throw new RuntimeException("AUT not fount when create server instance.");
    }

    public void execute() {
        boolean isDone = false;
        boolean isAgentDone = false;
        int pauseCount = 1;
        this.learningPoolServer.startLearningPool();
        serverInstance.createServerInstance();
//        while(!isDone && !this.learningPoolServer.getAgentDone()){
        while(!isDone){
            if (this.learningPoolServer.isLearningResultDTOQueueEmpty()) {
                if (this.learningPoolServer.getPauseAgent()) {
                    this.learningPoolServer.setPauseAgent(false);
                    LogHelper.info("Pause Agent " + pauseCount);
                    LogHelper.writeAllLog();
                    pauseCount++;
                }
            }
            while(!directiveTreeHelper.isTreeComplete()){
                List<CrawlerDirective> crawlerDirectives = directiveTreeHelper.takeFirstUnprocessedCrawlerDirectives();

                List<LearningTask> learningTaskList = crawler.crawlingWithDirectives(config, crawlerDirectives);
                for(LearningTask task: learningTaskList){
                    LOGGER.debug("The task is {}", task);
                    if(taskCompleteMap.get(task.getStateID()) == null && !inputPageUrls.contains(task.getTargetURL())){
                        LOGGER.debug("taskCompleteMap.get(task.getStateID()) == null && !isSimilarTask(task) is true");
                        taskCompleteMap.put(task.getStateID(), false);
                        inputPageUrls.add(task.getTargetURL());
                        learningPoolServer.enQueueLearningTaskDTO(LearningTaskDTOMapper.mappingLearningTaskDTOFrom(task));
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
            if (this.learningPoolServer.getAgentDone()) {
                if (isAgentDone) {
                    isDone = true;
                }
                isAgentDone = true;
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
        this.learningPoolServer.stopLearningPool();
        crawler.generateGraph();
        serverInstance.closeServerInstance();
    }

//    private boolean isSimilarTask(LearningTask targetTask) {
//        LOGGER.debug("isSimilarTask");
//        LOGGER.debug("The targetTask is {}", targetTask.toString());
//        for (LearningTask task : learningTasks) {
//            LOGGER.debug("The task is {}", task.toString());
//            LOGGER.debug("task.getFormXPath().equals(targetTask.getFormXPath()): {}", task.getFormXPath().equals(targetTask.getFormXPath()));
//            LOGGER.debug("task.getActionSequence().equals(targetTask.getActionSequence()): {}", task.getActionSequence().equals(targetTask.getActionSequence()));
//            if (task.getFormXPath().equals(targetTask.getFormXPath())
//                    && task.getActionSequence().equals(targetTask.getActionSequence())) {
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean checkCrawlingDone() {
        boolean isDone = true;
        for(Map.Entry<String, Boolean> entry: taskCompleteMap.entrySet()){
            if(!entry.getValue()){
                isDone = false;
                break;
            }
        }
//        if(learningPool.getTaskSize() != 0) isDone = false;
//        if(learningPool.getResultSize() != 0) isDone = false;
        if(!this.learningPoolServer.isLearningTaskDTOQueueEmpty()) isDone = false;
        if(!this.learningPoolServer.isLearningResultDTOQueueEmpty()) isDone = false;
        return isDone;
    }

    private List<LearningResult> waitAndGetLearningResults() {
        List<LearningResult> results;
        boolean isDone = true;
        while (isDone){
            isDone = !this.learningPoolServer.getAgentDone();
            results = this.getAllLearningResult();
            if(!results.isEmpty()){
                checkResultIsDone(results);
                return results;
            }
            if (this.learningPoolServer.getPauseAgent()) {
                break;
            }
            try {
                Thread.sleep(config.SLEEP_TIME * 1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return new ArrayList<>();
    }

    private void checkResultIsDone(List<LearningResult> results) {
        for(LearningResult result: results)
            if(result.isDone()) taskCompleteMap.put(result.getTaskID(), true);
    }

    private List<LearningResult> getAllLearningResult(){
        List<LearningResult> learningResultList = new ArrayList<>();
        while(!this.learningPoolServer.isLearningResultDTOQueueEmpty()){
            learningResultList.add(LearningResultDTOMapper.mappingLearningResultFrom(this.learningPoolServer.deQueueLearningResultDTO()));
        }
        return learningResultList;
    }

}
