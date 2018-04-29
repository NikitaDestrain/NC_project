package server.exportdata.config;

public class ExportConfigItem {
    private final String type;
    private final String parent;
    private final String strategy;

    public ExportConfigItem(String type, String parent, String strategy) {
        this.type = type;
        this.parent = parent;
        this.strategy = strategy;
    }

    public String getType() {
        return type;
    }

    public String getParent() {
        return parent;
    }

    public String getStrategy() {
        return strategy;
    }
}
