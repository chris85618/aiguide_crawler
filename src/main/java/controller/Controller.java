package controller;

import util.Config;

import java.util.Map;
import java.util.TreeMap;

public class Controller {
    Map<String, Boolean> taskCompleteMap;

    public Controller() {
        taskCompleteMap = new TreeMap<String, Boolean>();
    }

    public void execute(Config config){
        System.out.println("AUT is: " + config.AUT_NAME);
    }
}
