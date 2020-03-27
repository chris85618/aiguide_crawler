package crawler;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.state.StateFlowGraph;
import javafx.util.Pair;
import learning_data.LearningTask;
import ntut.edu.aiguide.crawljax.plugins.AIGuidePlugin;
import ntut.edu.aiguide.crawljax.plugins.domain.Action;
import ntut.edu.aiguide.crawljax.plugins.domain.State;
import ntut.edu.tw.irobot.CrawlJaxRunnerFactory;
import util.Config;
import util.HighLevelAction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Crawljax implements Crawler {

    private StateFlowGraph stateFlowGraph;

    public Crawljax() {
    }

    @Override
    public List<LearningTask> crawlingWithDirectives(Config config, Map<String, List<HighLevelAction>> crawlerDirectives) {
        AIGuidePlugin aiGuidePlugin = createAIGuidePlugin(crawlerDirectives);
        CrawljaxRunner crawljaxRunner = createCrawljaxRunner(config, aiGuidePlugin);
        crawljaxRunner.call();
        List<Pair<String, List<Action>>> learningTargets = aiGuidePlugin.getActionSequenceSet();
//        mergingGraph(aiGuidePlugin.getStateFlowGraph());
        return convertToLearningTask(learningTargets);
    }

    private List<LearningTask> convertToLearningTask(List<Pair<String, List<Action>>> learningTargets) {
        List<LearningTask> learningTasks = new LinkedList<>();
        for (Pair<String, List<Action>> learningSet : learningTargets) {
            learningTasks.add(new LearningTask(convertToUtilAction(learningSet.getValue()), learningSet.getKey()));
        }
        return learningTasks;
    }

    private List<util.Action> convertToUtilAction(List<Action> actions) {
        List<util.Action> convertAction = new LinkedList<>();
        for (Action action : actions) {
            convertAction.add(new util.Action(action.getActionXpath(), action.getValue()));
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
        return new AIGuidePlugin(directiveStack);
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
        crawljaxFactory.setDepth(config.getDepth());
        crawljaxFactory.setHeadLess(false);
        crawljaxFactory.setRecordMode(true);
        crawljaxFactory.setEventWaitingTime(500);
        crawljaxFactory.setPageWaitingTime(500);
        return crawljaxFactory.createCrawlerCrawlJaxRunner(config.url, aiGuidePlugin);
    }



    @Override
    public void generateGraph() {

    }
}
