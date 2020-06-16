import controller.Controller;
import util.Config;
import util.LogHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class main {

    public static void main(String[] args) throws Exception {
        LogHelper.info("AI GUIDE Start...");

        Config config = new Config("./configuration/configuration.json");
        copyVE("./variableElement/data/" + config.AUT_NAME + "/variableElementList.json");
        new Controller(config).execute();

        LogHelper.info("AI GUIDE Close...");
        LogHelper.writeAllLog();
    }

    private static void copyVE(String source) {
        try {
            File src = new File(source), dst = new File("./variableElement/variableElementList.json");
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            System.out.println("Not fount variableElementList...");
        }
    }
}
