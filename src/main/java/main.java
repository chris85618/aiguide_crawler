import controller.Controller;
import util.Config;


public class main {

    public static void main(String[] args) throws Exception {
        Config config = new Config("./configuration/configuration.json");
        new Controller(config).execute();
    }

}
