package server.exportdata.config;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ExportConfigParser {
    private Properties properties;
    private static ExportConfigParser instance;
    private Map<String, String> exportProperties;

    private final String INCORRECT_PROPERTY = "Unable to load the property: ";

    public static final String TASK_PROPERTY = "TASK";
    public static final String JOURNAL_PROPERTY = "JOURNAL";

    private ExportConfigParser(String path) throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream(path);
        properties.load(fis);
        fis.close();

        exportProperties = new HashMap<>();
        exportProperties.put(TASK_PROPERTY, get(TASK_PROPERTY));
        exportProperties.put(JOURNAL_PROPERTY, get(JOURNAL_PROPERTY));
    }

    public static ExportConfigParser getInstance(String path) throws IOException {
        if (instance == null)
            instance = new ExportConfigParser(path);
        return instance;
    }

    public static ExportConfigParser getInstance() {
        return instance;
    }

    private String get(String key) throws IllegalPropertyException {
        String s = properties.getProperty(key);
        if (s == null) {
            throw new IllegalPropertyException(INCORRECT_PROPERTY + key);
        } else {
            return s;
        }
    }

    public String getProperty(String key) {
        return exportProperties.get(key);
    }
}
