package crawler;

import directive_tree.CrawlerDirective;
import usecase.learningPool.formInputValueList.FormInputValueList;
import usecase.learningPool.learningTask.LearningTask;
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
import static org.junit.Assert.assertTrue;

public class CrawlerTest {
    private Crawler crawler;
    private Config config;
    private ServerInstanceManagement serverInstanceManagement;
    @Before
    public void setUp() throws Exception {
        this.config = new Config("./src/test/configuration/crawlerTestConfiguration.json");
        this.serverInstanceManagement = new TimeOffManagementServer(config.AUT_NAME, config.AUT_PORT);
        this.serverInstanceManagement.createServerInstance();
        this.crawler = new Crawljax(serverInstanceManagement);
    }

    @After
    public void tearDown() throws Exception {
        this.serverInstanceManagement.closeServerInstance();
    }

    @Test
    public void testWillGetThreeLearningTask() {
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new ArrayList<>());
        System.out.println(result.get(0).getStateID());
        assertEquals(3, result.size());
        System.out.println(result.get(1).getStateID());
        System.out.println(result.get(2).getStateID());
    }

    @Test
    public void testGiveOneDirectiveAndWillGetOneDifferentLearningTask() {
        // first iteration
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new ArrayList<>());
        final LearningTask firstLearningTask = result.get(0);
        final String firstStateID = firstLearningTask.getStateID();

        final String firstDom = firstLearningTask.getDom();

        String actionXpath = "/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[2]".toUpperCase();
        Action action = new Action(actionXpath, "");
        List<Action> actionSet = new LinkedList<>();
        actionSet.add(action);
        HighLevelAction highLevelAction = new HighLevelAction(actionSet);

        Map<String, List<HighLevelAction>> directive = new HashMap<>();
        directive.put(firstStateID, Collections.singletonList(highLevelAction));
        CrawlerDirective crawlerDirective = new CrawlerDirective(
                firstStateID,
                firstDom,
                Collections.singletonList(highLevelAction),
                new FormInputValueList()
        );
        List<CrawlerDirective> directives = new ArrayList<>();
        directives.add(crawlerDirective);

        // second iteration
        result = crawler.crawlingWithDirectives(config, directives);
        assertEquals(1, result.size());
        List<List<util.Action>> actions = result.get(0).getActionSequence();
        assertEquals(1, actions.size());
        assertEquals(actionXpath, actions.get(0).get(0).getXpath());
    }

    @Test
    public void testGiveTwoDirectiveAndWillGetThreeLearningTask() {
        // first iteration
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new ArrayList<>());
        final LearningTask firstLearningTask = result.get(0);
        final String firstStateID = String.valueOf(firstLearningTask.getStateID());

        final String firstDom = firstLearningTask.getDom();

        List<HighLevelAction> highLevelActionLogin = createLoginPageHighLevelActions();

        CrawlerDirective firstCrawlerDirective = new CrawlerDirective(
                firstStateID,
                firstDom,
                highLevelActionLogin,
                new FormInputValueList()
        );
        List<CrawlerDirective> directives = new ArrayList<>();
        directives.add(firstCrawlerDirective);
        
        // second iteration
        result = crawler.crawlingWithDirectives(config, directives);

        assertTrue(result.size() > 0);

        final LearningTask secondLearningTask = result.get(0);
        final String secondStateID = String.valueOf(secondLearningTask.getStateID());

        final String secondDom = secondLearningTask.getDom();

        HighLevelAction highLevelAction_1 = createPerformAllValidHighLevelAction();
        HighLevelAction highLevelAction_2 = createPerformClickCreateHighLevelAction();

        List<HighLevelAction> highLevelActions = new LinkedList<>();
        highLevelActions.add(highLevelAction_1);
        highLevelActions.add(highLevelAction_2);

        CrawlerDirective secondCrawlerDirective = new CrawlerDirective(
            secondStateID,
            secondDom,
            highLevelActions,
            new FormInputValueList()
        );

        List<CrawlerDirective> directives_2 = new ArrayList<>();
        directives.add(secondCrawlerDirective);
        directives.add(firstCrawlerDirective);

        // third iteration
        result = crawler.crawlingWithDirectives(config, directives_2);

        for (LearningTask learningTask : result) {
            System.out.println(learningTask.getTargetURL());
            System.out.println(learningTask.getActionSequence().size());
            System.out.println(Arrays.toString(learningTask.getCoverage()));
        }
        assertEquals(3, result.size());
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

    private List<HighLevelAction> createLoginPageHighLevelActions() {
        Action action_1 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[1]".toUpperCase(), "teacher@ntut.tw");
        Action action_1_copy = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[1]".toUpperCase(), "teacher@ntut.tw");
        Action action_2 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[3]/DIV[1]/INPUT[1]".toUpperCase(), "1234");
        Action action_2_copy = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[3]/DIV[1]/INPUT[1]".toUpperCase(), "1234");
        Action action_3 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[4]/DIV[1]/BUTTON[1]".toUpperCase(), null);
        Action action_3_copy = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[4]/DIV[1]/BUTTON[1]".toUpperCase(), null);
        Action action_4 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[1]".toUpperCase(), "student@ntut.tw");
        Action action_5 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[3]/DIV[1]/INPUT[1]".toUpperCase(), "ab5sRsda.ad");
        Action action_6 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[2]/DIV[1]/INPUT[1]".toUpperCase(), "teacher@ntut.tw");
        Action action_7 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[3]/DIV[1]/INPUT[1]".toUpperCase(), "1234");
        Action action_8 = new Action("/HTML[1]/BODY[1]/DIV[1]/FORM[1]/DIV[4]/DIV[2]/P[1]/A[2]".toUpperCase(), null);
        List<Action> actionSet_1 = new LinkedList<>();
        List<Action> actionSet_1_copy = new LinkedList<>();
        actionSet_1.add(action_1);
        actionSet_1.add(action_2);
        actionSet_1_copy.add(action_1_copy);
        actionSet_1_copy.add(action_2_copy);

        List<Action> actionSet_2 = new LinkedList<>();
        List<Action> actionSet_2_copy = new LinkedList<>();
        actionSet_2.add(action_3);
        actionSet_2_copy.add(action_3_copy);

        List<Action> actionSet_3 = new LinkedList<>();
        actionSet_3.add(action_4);
        actionSet_3.add(action_5);

        List<Action> actionSet_4 = new LinkedList<>();
        actionSet_4.add(action_6);
        actionSet_4.add(action_7);

        List<Action> actionSet_5 = new LinkedList<>();
        actionSet_5.add(action_8);

        HighLevelAction highLevelAction_1 = new HighLevelAction(actionSet_1);
        HighLevelAction highLevelAction_1_copy = new HighLevelAction(actionSet_1_copy);
        HighLevelAction highLevelAction_2 = new HighLevelAction(actionSet_2);
        HighLevelAction highLevelAction_2_copy = new HighLevelAction(actionSet_2_copy);
        HighLevelAction highLevelAction_3 = new HighLevelAction(actionSet_3);
        HighLevelAction highLevelAction_4 = new HighLevelAction(actionSet_4);
        HighLevelAction highLevelAction_5 = new HighLevelAction(actionSet_5);

        List<HighLevelAction> highLevelActions = new LinkedList<>();
        highLevelActions.add(highLevelAction_1);
        highLevelActions.add(highLevelAction_1_copy);
        highLevelActions.add(highLevelAction_2);
        highLevelActions.add(highLevelAction_3);
        highLevelActions.add(highLevelAction_2_copy);
        highLevelActions.add(highLevelAction_4);
        highLevelActions.add(highLevelAction_5);

        return highLevelActions;
    }
}
