package crawler;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.state.StateFlowGraph;
import learning_data.LearningTask;
import ntut.edu.aiguide.crawljax.plugins.AIGuidePlugin;
import ntut.edu.aiguide.crawljax.plugins.domain.Action;
import ntut.edu.aiguide.crawljax.plugins.domain.LearningTarget;
import ntut.edu.aiguide.crawljax.plugins.domain.State;
import ntut.edu.tw.irobot.CrawlJaxRunnerFactory;
import server_instance.ServerInstanceManagement;
import util.Config;
import util.HighLevelAction;

import java.util.*;

public class Crawljax implements Crawler {

    private StateFlowGraph stateFlowGraph;
    private final ServerInstanceAdapter serverInstanceManagement;
    private Map<String, String> domHashCompareTable;

    public Crawljax(ServerInstanceManagement serverInstance) {
        this.serverInstanceManagement = new ServerInstanceAdapter(serverInstance);
        this.domHashCompareTable = new HashMap<>();
    }

    @Override
    public List<LearningTask> crawlingWithDirectives(Config config, Map<String, List<HighLevelAction>> crawlerDirectives) {
        serverInstanceManagement.recordCoverage();
        AIGuidePlugin aiGuidePlugin = createAIGuidePlugin(crawlerDirectives, config.AUT_PORT);
        CrawljaxRunner crawljaxRunner = createCrawljaxRunner(config, aiGuidePlugin);
        crawljaxRunner.call();
        List<LearningTarget> learningTargets = aiGuidePlugin.getLearningTarget();
        int branchCoverageCounter = 0, statementCoverageCounter = 0;
        for(int i: serverInstanceManagement.getTotalStatementCoverage()) if(i == 300) statementCoverageCounter++;
        for(int i: serverInstanceManagement.getTotalBranchCoverage()) if(i == 300) branchCoverageCounter++;
        System.out.println("**************** Current statement coverage: " + statementCoverageCounter + " ****************");
        System.out.println("**************** Current branch coverage: " + branchCoverageCounter + " ****************");
        System.out.println("**************** Current crawlerDirectives size: " + crawlerDirectives.size() + " ****************");
//        mergingGraph(aiGuidePlugin.getStateFlowGraph());
        return convertToLearningTask(learningTargets);
    }

    private List<LearningTask> convertToLearningTask(List<LearningTarget> learningTargets) {
        List<LearningTask> learningTasks = new LinkedList<>();

        for (LearningTarget learningTarget : learningTargets) {
            String stateID = (learningTarget.getDom() + Arrays.toString(serverInstanceManagement.getTotalBranchCoverage())).hashCode() + "";
            String domHash = learningTarget.getDom().hashCode() + "";
            int coverage_counter = 0;
            for(int i: serverInstanceManagement.getTotalBranchCoverage()) if(i == 300) coverage_counter++;
            System.out.println("--------------------------------------------------------------");
            System.out.println("coverage num: " + coverage_counter);
            System.out.println("stateID: " + stateID);
            System.out.println("--------------------------------------------------------------");
            domHashCompareTable.put(stateID, domHash);
            learningTasks.add(new LearningTask(convertToUtilAction(learningTarget.getActionSequence()),
                                                serverInstanceManagement.getTotalBranchCoverage(),
                                                learningTarget.getTargetURL(),
                                                stateID,
                                                new HashMap<>()));
        }
        return learningTasks;
    }

    private List<List<util.Action>> convertToUtilAction(List<List<Action>> actions) {
        List<List<util.Action>> convertAction = new LinkedList<>();
        for (List<Action> actionSequence : actions) {
            List<util.Action> convertActionSequence = new ArrayList<>(actionSequence.size());
            for (Action action : actionSequence)
                convertActionSequence.add(new util.Action(action.getActionXpath(), action.getValue()));
            convertAction.add(convertActionSequence);
        }
        return convertAction;
    }

    /**
     *
     * @param crawlerDirectives
     *          the order is leaf to root
     * @param AUT_PORT
     * @return
     */
    private AIGuidePlugin createAIGuidePlugin(Map<String, List<HighLevelAction>> crawlerDirectives, int AUT_PORT) {
        Stack<State> directiveStack = new Stack<>();
        for (Map.Entry<String, List<HighLevelAction>> set : crawlerDirectives.entrySet())
            directiveStack.push(createCrawlerState(domHashCompareTable.get(set.getKey()), set.getValue()));
        return new AIGuidePlugin(directiveStack, serverInstanceManagement, AUT_PORT);
    }

    private State createCrawlerState(String domHash, List<HighLevelAction> highLevelActions) {
        LinkedList<List<Action>> actions = new LinkedList<>();
        for (HighLevelAction action : highLevelActions)
            actions.add(transferToCrawlerAction(action.getActionSequence()));
        System.out.println(actions);
        return new State(domHash, actions);
    }

    private List<Action> transferToCrawlerAction(List<util.Action> actionSequence) {
        List<Action> transferAction = new LinkedList<>();
        for(util.Action action : actionSequence)
            transferAction.add(new Action(action.getXpath(), action.getValue()));
        return transferAction;
    }

    private CrawljaxRunner createCrawljaxRunner(Config config, AIGuidePlugin aiGuidePlugin) {
        CrawlJaxRunnerFactory crawljaxFactory = new CrawlJaxRunnerFactory();
        crawljaxFactory.setDepth(config.CRAWLER_DEPTH);
        crawljaxFactory.setHeadLess(false);
        crawljaxFactory.setRecordMode(true);
        crawljaxFactory.setClickOnce(true);
        crawljaxFactory.setEventWaitingTime(1000);
        crawljaxFactory.setPageWaitingTime(1000);
        return crawljaxFactory.createCrawlerCrawlJaxRunner(config.ROOT_URL, aiGuidePlugin);
    }

    @Override
    public void generateGraph() {

    }

    @Override
    public Map getTotalCoverage() {
        Map<String, Integer> summary = new HashMap<>();
        int coverage_counter = 0;
        for(int i: serverInstanceManagement.getTotalStatementCoverage()) if(i != 0) coverage_counter++;
        summary.put("statement", coverage_counter);
        coverage_counter = 0;
        for(int i: serverInstanceManagement.getTotalBranchCoverage()) if(i != 0) coverage_counter++;
        summary.put("branch", coverage_counter);
        return summary;
    }
}
