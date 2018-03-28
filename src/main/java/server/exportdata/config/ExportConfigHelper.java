package server.exportdata.config;

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
            taskStrategy = Integer.parseInt(parser.getProperty(ExportConfigParser.TASK_PROPERTY));
            journalStrategy = Integer.parseInt(parser.getProperty(ExportConfigParser.JOURNAL_PROPERTY));
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
}
