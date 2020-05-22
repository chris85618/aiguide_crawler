package directive_tree;

import learning_data.LearningResult;
import learning_data.LearningTask;
import util.HighLevelAction;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DirectiveTreeHelper {
    private final Directive DTRoot;
    private Queue<Directive> unprocessedLeaves;
    private Directive processingLeaf;

    public DirectiveTreeHelper() {
        this.DTRoot = new Directive(null, null);
        this.unprocessedLeaves = new LinkedList<>();
        this.unprocessedLeaves.offer(this.DTRoot);
    }

    public void addDirectives(List<LearningResult> results) {
        for(LearningResult result : results){
            InputPage page = DTRoot.findInputPageByStateID(result.getTaskID());
            if(result.isDone())
                page.setDone();
            else{
                Directive d = convertToDirective(result);
                unprocessedLeaves.offer(d);
                page.addDirective(d);
            }
        }
    }

    public void addInputPage(LearningTask task) {
        InputPage ip = convertToInputPage(task);
        processingLeaf.addInputPage(ip);
    }

    public LinkedHashMap<String, List<HighLevelAction>> takeFirstUnprocessedCrawlerDirectives() {
        processingLeaf = unprocessedLeaves.poll();
        if(processingLeaf == null) return null;
        return getCrawlerDirectives(processingLeaf);
    }

    public Boolean isTreeComplete() {
        return unprocessedLeaves.isEmpty();
    }

    private LinkedHashMap<String, List<HighLevelAction>> getCrawlerDirectives(Directive d) {
        LinkedHashMap<String, List<HighLevelAction>> crawlerDirectives = new LinkedHashMap<>();
        Directive currentDirective = d;
        assert d != null: "Directive is null in getCrawlerDirectives method";
        while(!currentDirective.isDTRoot()){
            InputPage ip = currentDirective.getParent();
            crawlerDirectives.put(ip.getStateID(), currentDirective.getActionSequence());
            currentDirective = ip.getParent();
        }
        return crawlerDirectives;
    }

    private InputPage convertToInputPage(LearningTask task) {
        return new InputPage(processingLeaf, task.getStateID(), task.getTargetURL());
    }

    private Directive convertToDirective(LearningResult result) {
        return new Directive(DTRoot.findInputPageByStateID(result.getTaskID()), result.getActionSequence());
    }

    public void printDirectiveTree() {
        System.out.println("=========================DirectiveTree=========================");
        printAllNode(DTRoot);
        System.out.println("===============================================================");
    }

    public void drawDirectiveTree(){
        GraphDrawer graphDrawer = new GraphDrawer();
        graphDrawer.draw(this.DTRoot);
    }

    private void printAllNode(Directive directive) {
        System.out.println(directive.toString());
        for (InputPage ip : directive.getChild()) {
            System.out.println(ip.toString());
            for (Directive d : ip.getChild())
                printAllNode(d);
        }
    }
}
