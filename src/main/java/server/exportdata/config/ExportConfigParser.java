package server.exportdata.config;


import server.exportdata.ExportConstants;

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

    private ExportConfigParser(String path) throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream(path);
        properties.load(fis);
        fis.close();

        exportProperties = new HashMap<>();
        exportProperties.put(ExportConstants.TASK_PROPERTY, get(ExportConstants.TASK_PROPERTY));
        exportProperties.put(ExportConstants.JOURNAL_PROPERTY, get(ExportConstants.JOURNAL_PROPERTY));
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
