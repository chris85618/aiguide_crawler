package crawler;
import util.Config;
import util.Action;

import java.util.List;
import java.util.Map;

public interface Crawler {
    void crawlingWithDirectives(Config config, Map<String, List<Action>> crawlerDirectives);
    void generateGraph();
}
