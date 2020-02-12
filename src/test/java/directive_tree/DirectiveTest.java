package directive_tree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectiveTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetActionSequenceIsNull() {
        Directive root;
        root = new Directive(null, null);
        assertNull(root.getActionSequence());
    }

    @Test
    public void testAddInputPage() {
    }

    @Test
    public void testGetInputPageListSize() {
    }

    @Test
    public void testContainPage() {
    }

    @Test
    public void testGetChild() {
    }

    @Test
    public void testFindChildByTask() {
    }
}