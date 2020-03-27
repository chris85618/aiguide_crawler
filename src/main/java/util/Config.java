package util;

public class Config {
    public final String AUT_NAME = "timeoff-management";
    public String url = "";
    private int depth;

    public void setCrawlingRootUrl(String url) {
        this.url = url;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
