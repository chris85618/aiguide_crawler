package directive_tree;

import java.util.LinkedList;
import java.util.Queue;

public class DirectiveTreeHelper {
    private final Directive DTRoot;
    private Queue<Directive> unprocessedLeaves;
    private Directive processingLeaf;

    public DirectiveTreeHelper() {
        this.DTRoot = new Directive(null, null);
        this.unprocessedLeaves = new LinkedList<>();
    }
}
