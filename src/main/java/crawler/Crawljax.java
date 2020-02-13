package crawler;

import learning_data.LearningTask;
import util.Action;
import util.Config;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Crawljax implements Crawler {

    public Crawljax() {

    }

    @Override
    public List<LearningTask> crawlingWithDirectives(Config config, Map<String, List<Action>> crawlerDirectives) {
        LinkedList<LearningTask> learningTasks = new LinkedList<>();
        return learningTasks;
    }

    @Override
    public void generateGraph() {

    }
}
