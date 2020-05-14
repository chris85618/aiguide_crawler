package directive_tree;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {
    private Graph graph;

    @Before
    public void setUp() throws Exception {
        this.graph = new Graph();
    }

//    @Test
//    public void testSaveFile(){
//        String filePath = "./src/test/java/directive_tree/test.txt";
//        this.graph.setTreeStructure(new JsonObject());
//        this.graph.saveFile(filePath, "./directiveTreeGraphic/d3jsFomat.html");
//        File directiveTreeFile = new File(filePath);
//        assertTrue(directiveTreeFile.exists());
//    }

    @Test
    public void testReadFile(){
        String filePath = "./src/test/java/directive_tree/readTest.txt";
        String fileText = this.graph.getFileText(filePath);
        assertEquals("test read file\ntest write file %1", fileText);
    }

    @Test
    public void testWriteAndSaveFile(){
        String filePath = "./src/test/java/directive_tree/writeTest.txt";
        String writeText = "test write file\ntest write file %1";
        this.graph.writeFile(filePath, writeText);
        String readFileText = this.graph.getFileText(filePath);
        assertEquals(writeText, readFileText);
    }

    @Test
    public void testGetPreviousLevelPathWhenFilePathIsFullPath(){
        String filePath = "./src/test/java/directive_tree/writeTest.txt";
        assertEquals("./src/test/java/directive_tree", this.graph.getPreviousLevePath(filePath));
    }

    @Test
    public void testGetPreviousLevelPathWhenFilePathIsOnlyFileName(){
        String filePath = "writeTest.txt";
        assertEquals("./", this.graph.getPreviousLevePath(filePath));
    }
}
