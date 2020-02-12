import controller.Controller;
import util.Config;

public class main {

    public static void main(String[] args) throws Exception {
        new Controller().execute(new Config());
    }

}
