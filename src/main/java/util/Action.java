package util;

public class Action {
    private final String xpath;
    private final String value;

    public Action(String xpath, String value) {
        this.xpath = xpath;
        this.value = value;
    }

    public Action(String xpath) {
        this.xpath = xpath;
        this.value = null;
    }

    public String getXpath() {
        return xpath;
    }

    public String getValue() {
        return value;
    }
}
