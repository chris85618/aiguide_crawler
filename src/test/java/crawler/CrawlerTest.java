package crawler;

import learning_data.LearningTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Config;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

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
        Config config = new Config();
        config.setCrawlingRootUrl("https://www.google.com/");
        List<LearningTask> result = crawler.crawlingWithDirectives(config, new HashMap<>());
        assertEquals(1, result.size());
    }

}
