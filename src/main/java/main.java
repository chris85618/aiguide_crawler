import adpater.controller.Controller;
import util.Config;
import util.LogHelper;


public class main {

    public static void main(String[] args) throws Exception {
        LogHelper.info("AI GUIDE Start...");

        Config config = new Config("./configuration/configuration.json");
        new Controller(config).execute();

        LogHelper.info("AI GUIDE Close...");
        LogHelper.writeAllLog();
    }
}
