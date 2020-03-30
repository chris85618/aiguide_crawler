package crawler;

import com.google.common.collect.ImmutableMap;
import learning_data.LearningTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server_instance.ServerInstanceManagement;
import server_instance.TimeOffManagementServer;
import util.Action;
import util.Config;
import util.HighLevelAction;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CrawlerTest {
    private Crawler crawler;
    private Config config;
    private ServerInstanceManagement serverInstanceManagement;
    @Before
    public void setUp() throws Exception {
        this.crawler = new Crawljax();
        this.config = new Config("./src/test/configuration/crawlerTestConfiguration.json");
        this.serverInstanceManagement = new TimeOffManagementServer(config.AUT_PORT);
        this.serverInstanceManagement.createServerInstance();
    }

    @After
    public void tearDown() throws Exception {
        this.serverInstanceManagement.closeServerInstance();
    }

    @Test
    public void testWillGetOneLearningTask() {
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new HashMap<>());
        System.out.println(result.get(0).getStateID());
        assertEquals(1, result.size());
    }

    @Test
    public void testGiveOneDirectiveAndWillGetOneDifferentLearningTask() {
        // first iteration
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new HashMap<>());
        String firstStateID = result.get(0).getStateID();

        String actionXpath = "/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[2]".toUpperCase();
        Action action = new Action(actionXpath, "");
        List<Action> actionSet = new LinkedList<>();
        actionSet.add(action);
        HighLevelAction highLevelAction = new HighLevelAction(actionSet);

        Map<String, List<HighLevelAction>> directive = new HashMap<>();
        directive.put(firstStateID, Collections.singletonList(highLevelAction));

        // second iteration
        result = crawler.crawlingWithDirectives(config, directive);
        assertEquals(1, result.size());
        List<Action> actions = result.get(0).getActionSequence();
        assertEquals(1, actions.size());
        assertEquals(actionXpath, actions.get(0).getXpath());
    }

    @Test
    public void testGiveTwoDirectiveAndWillGetNineLearningTask() {
        // first iteration
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new HashMap<>());
        String firstStateID = result.get(0).getStateID();

        String actionXpath = "/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[2]".toUpperCase();
        Action action = new Action(actionXpath, "");
        List<Action> actionSet = new LinkedList<>();
        actionSet.add(action);
        HighLevelAction highLevelAction = new HighLevelAction(actionSet);

        Map<String, List<HighLevelAction>> directives = new HashMap<>();
        directives.put(firstStateID, Collections.singletonList(highLevelAction));

        // second iteration
        result = crawler.crawlingWithDirectives(config, ImmutableMap.copyOf(directives));

        String secondState = result.get(0).getStateID();


        HighLevelAction highLevelAction_1 = createPerformAllValidHighLevelAction();
        HighLevelAction highLevelAction_2 = createPerformClickCreateHighLevelAction();

        List<HighLevelAction> highLevelActions = new LinkedList<>();
        highLevelActions.add(highLevelAction_1);
        highLevelActions.add(highLevelAction_2);
        Map<String, List<HighLevelAction>> directive_2 = new LinkedHashMap<>();

        directive_2.put(secondState, highLevelActions);
        directive_2.put(firstStateID, Collections.singletonList(highLevelAction));

        // third iteration
        result = crawler.crawlingWithDirectives(config, directive_2);

        for (LearningTask learningTask : result) {
            System.out.println(learningTask.getStateID());
            System.out.println(learningTask.getRootURL());
        }
        assertEquals(9, result.size());

    }

    private HighLevelAction createPerformClickCreateHighLevelAction() {
        Action action_1 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[9]/div[1]/button[1]".toUpperCase(), "");
        List<Action> actionSet = new LinkedList<>(Collections.singleton(action_1));
        return new HighLevelAction(actionSet);
    }

    private HighLevelAction createPerformAllValidHighLevelAction() {
        Action action_1 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[1]/div[1]/input[1]".toUpperCase(), "abc");
        Action action_2 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[2]/div[1]/input[1]".toUpperCase(), "abc");
        Action action_3 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[3]/div[1]/input[1]".toUpperCase(), "abc");
        Action action_4 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[4]/div[1]/input[1]".toUpperCase(), "test@gmail.com");
        Action action_5 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[5]/div[1]/input[1]".toUpperCase(), "test@gmail.com");
        Action action_6 = new Action("/html[1]/body[1]/div[1]/div[3]/div[1]/form[1]/div[6]/div[1]/input[1]".toUpperCase(), "test@gmail.com");
        List<Action> actionSet = new LinkedList<>();
        actionSet.add(action_1);
        actionSet.add(action_2);
        actionSet.add(action_3);
        actionSet.add(action_4);
        actionSet.add(action_5);
        actionSet.add(action_6);
        return new HighLevelAction(actionSet);
    }
}
