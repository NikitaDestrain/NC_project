package server.exportdata.config;


import server.exportdata.ExportConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
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
        readAllProperties();
    }

    private void readAllProperties() {
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            exportProperties.put(key, value);
        }
    }

    public static ExportConfigParser getInstance(String path) throws IOException {
        if (instance == null)
            instance = new ExportConfigParser(path);
        return instance;
    }

    public static ExportConfigParser getInstance() {
        return instance;
    }

    public String getPropertyValue(String key) {
        return exportProperties.get(key);
    }
}
