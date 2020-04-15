package crawler;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.state.StateFlowGraph;
import javafx.util.Pair;
import learning_data.LearningTask;
import ntut.edu.aiguide.crawljax.plugins.AIGuidePlugin;
import ntut.edu.aiguide.crawljax.plugins.domain.Action;
import ntut.edu.aiguide.crawljax.plugins.domain.State;
import ntut.edu.tw.irobot.CrawlJaxRunnerFactory;
import server_instance.ServerInstanceManagement;
import util.Config;
import util.HighLevelAction;

import java.util.*;

public class Crawljax implements Crawler {

    private StateFlowGraph stateFlowGraph;
    private final ServerInstanceAdapter serverInstanceManagement;

    public Crawljax(ServerInstanceManagement serverInstance) {
        this.serverInstanceManagement = new ServerInstanceAdapter(serverInstance);
    }

    @Override
    public List<LearningTask> crawlingWithDirectives(Config config, Map<String, List<HighLevelAction>> crawlerDirectives) {
        serverInstanceManagement.recordCoverage();
        AIGuidePlugin aiGuidePlugin = createAIGuidePlugin(crawlerDirectives);
        CrawljaxRunner crawljaxRunner = createCrawljaxRunner(config, aiGuidePlugin);
        crawljaxRunner.call();
        List<Pair<String, List<List<Action>>>> learningTargets = aiGuidePlugin.getActionSequenceSet();
//        mergingGraph(aiGuidePlugin.getStateFlowGraph());
        return convertToLearningTask(learningTargets);
    }

    private List<LearningTask> convertToLearningTask(List<Pair<String, List<List<Action>>>> learningTargets) {
        List<LearningTask> learningTasks = new LinkedList<>();
        for (Pair<String, List<List<Action>>> learningSet : learningTargets) {
            learningTasks.add(new LearningTask(convertToUtilAction(learningSet.getValue()),
                                                serverInstanceManagement.getTotalCoverage(),
                                                "",
                                                learningSet.getKey(),
                                                new HashMap<String, String>()));
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
     * @return
     */
    private AIGuidePlugin createAIGuidePlugin(Map<String, List<HighLevelAction>> crawlerDirectives) {
        Stack<State> directiveStack = new Stack<>();
        for (Map.Entry<String, List<HighLevelAction>> set : crawlerDirectives.entrySet())
            directiveStack.push(createCrawlerState(set.getKey(), set.getValue()));
        return new AIGuidePlugin(directiveStack, serverInstanceManagement);
    }

    private State createCrawlerState(String domHash, List<HighLevelAction> highLevelActions) {
        LinkedList<List<Action>> actions = new LinkedList<>();
        for (HighLevelAction action : highLevelActions)
            actions.add(transferToCrawlerAction(action.getActionSequence()));
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
        crawljaxFactory.setEventWaitingTime(500);
        crawljaxFactory.setPageWaitingTime(500);
        return crawljaxFactory.createCrawlerCrawlJaxRunner(config.ROOT_URL, aiGuidePlugin);
    }

    @Override
    public void generateGraph() {

    }
}
