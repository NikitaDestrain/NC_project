package server.exportdata.config;

import server.exportdata.ExportConstants;

import java.util.HashMap;
import java.util.Map;

public class ExportConfigHelper {
    private static ExportConfigHelper ourInstance;
    private ExportConfigParser parser = ExportConfigParser.getInstance();

    private Map<String, ExportConfigItem> strategies;

    public static ExportConfigHelper getInstance() throws NumberFormatException {
        if(ourInstance == null) ourInstance = new ExportConfigHelper();
        return ourInstance;
    }

    private ExportConfigHelper() throws NumberFormatException {
        strategies = new HashMap<>();
        strategies.put(ExportConstants.JOURNAL_PROPERTY, new ExportConfigItem(ExportConstants.JOURNAL_PROPERTY, null, parser.getPropertyValue(ExportConstants.JOURNAL_PROPERTY)));
        strategies.put(ExportConstants.TASK_PROPERTY, new ExportConfigItem(ExportConstants.TASK_PROPERTY, ExportConstants.JOURNAL_PROPERTY, parser.getPropertyValue(ExportConstants.TASK_PROPERTY)));
    }

    public ExportConfigItem getStrategy(String type) {
        return strategies.get(type);
    }

    // todo vlla ничего не мешает хранить мапу стратегий с доступом по типу даных.
    // в этом случае для получения типа стратегии будет достаточно одного метода
}
