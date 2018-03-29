package server.exportdata;

public class ExportConstants {
    public static final int ONLY_JOURNAL_EXPORT_STRATEGY = 0;
    public static final int JOURNAL_WITH_TASKS_EXPORT_STRATEGY = 1;

    public static final int ONLY_TASK_EXPORT_STRATEGY = 2;
    public static final int TASK_WITH_JOURNAL_EXPORT_STRATEGY = 3;

    public static final String PATH_TO_PROPERTIES = "properties/exportconfig.properties";

    public static final String TASK_PROPERTY = "TASK";
    public static final String JOURNAL_PROPERTY = "JOURNAL";

    // EXCEPTION MESSAGES

    public static final String INCORRECT_PARAMETER = "Unable to perform the export due to the incorrect received parameter!";
}
