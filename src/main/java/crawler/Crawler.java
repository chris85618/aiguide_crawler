package crawler;

import learning_data.LearningTask;
import util.Config;
import util.HighLevelAction;

import java.util.List;
import java.util.Map;

public interface Crawler {
    List<LearningTask> crawlingWithDirectives(Config config, Map<Integer, List<HighLevelAction>> crawlerDirectives);
    void generateGraph();
}
