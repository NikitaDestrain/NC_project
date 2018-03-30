package server.exportdata.config;

import server.exportdata.ExportConstants;

public class ExportConfigHelper {
    private static ExportConfigHelper ourInstance = new ExportConfigHelper();
    private ExportConfigParser parser = ExportConfigParser.getInstance();

    private Integer taskStrategy;
    private Integer journalStrategy;

    public static ExportConfigHelper getInstance() {
        return ourInstance;
    }

    private ExportConfigHelper() {
        try {
            taskStrategy = Integer.parseInt(parser.getProperty(ExportConstants.TASK_PROPERTY));
            journalStrategy = Integer.parseInt(parser.getProperty(ExportConstants.JOURNAL_PROPERTY));
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    public Integer getTaskStrategy() {
        return taskStrategy;
    }

    public Integer getJournalStrategy() {
        return journalStrategy;
    }

    // todo vlla ничего не мешает хранить мапу стратегий с доступом по типу даных.
    // в этом случае для получения типа стратегии будет достаточно одного метода
}
