package crawler;

import learning_data.LearningTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Action;
import util.Config;
import util.HighLevelAction;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CrawlerTest {
    Crawler crawler;

    @Before
    public void setUp() throws Exception {
        this.crawler = new Crawljax();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWillGetOneLearningTask() {
        Config config = new Config("./src/test/configuration/configuration.json");
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new HashMap<>());
        System.out.println(result.get(0).getStateID());
        assertEquals(1, result.size());
    }

    @Test
    public void testGiveOneDirectiveAndWillGetOneDifferentLearningTask() {
        Config config = new Config("./src/test/configuration/configuration.json");
        Action action = new Action("/html[1]/body[1]/div[1]/form[1]/div[4]/div[2]/p[1]/a[2]".toUpperCase(), "");
        List<Action> actionSet = new LinkedList<>();
        actionSet.add(action);
        HighLevelAction highLevelAction = new HighLevelAction(actionSet);

        Map<String, List<HighLevelAction>> directive = new HashMap<>();
        directive.put("-1439788695", Collections.singletonList(highLevelAction));
        List<LearningTask> result = crawler.crawlingWithDirectives(config, directive);
        assertEquals(1, result.size());
    }
}
