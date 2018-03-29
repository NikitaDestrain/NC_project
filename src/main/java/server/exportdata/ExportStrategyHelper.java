package server.exportdata;

import server.exportdata.exportjournal.JournalWithTasksExportStrategy;
import server.exportdata.exportjournal.OnlyJournalExportStrategy;
import server.exportdata.exporttask.OnlyTaskExportStrategy;
import server.exportdata.exporttask.TaskWithJournalExportStrategy;

import java.util.HashMap;
import java.util.Map;

public class ExportStrategyHelper {
    private static ExportStrategyHelper ourInstance = new ExportStrategyHelper();
    private Map<Integer, ExportStrategy> strategies;

    public static ExportStrategyHelper getInstance() {
        return ourInstance;
    }

    private ExportStrategyHelper() {
        strategies = new HashMap<>();
        strategies.put(ExportConstants.ONLY_JOURNAL_EXPORT_STRATEGY, new OnlyJournalExportStrategy());
        strategies.put(ExportConstants.JOURNAL_WITH_TASKS_EXPORT_STRATEGY, new JournalWithTasksExportStrategy());
        strategies.put(ExportConstants.ONLY_TASK_EXPORT_STRATEGY, new OnlyTaskExportStrategy());
        strategies.put(ExportConstants.TASK_WITH_JOURNAL_EXPORT_STRATEGY, new TaskWithJournalExportStrategy());
    }

    public ExportStrategy resolveStrategy(int strategy) throws ExportException {
        switch (strategy) {
            case ExportConstants.ONLY_JOURNAL_EXPORT_STRATEGY:
                return strategies.get(ExportConstants.ONLY_JOURNAL_EXPORT_STRATEGY);
            case ExportConstants.JOURNAL_WITH_TASKS_EXPORT_STRATEGY:
                return strategies.get(ExportConstants.JOURNAL_WITH_TASKS_EXPORT_STRATEGY);
            case ExportConstants.ONLY_TASK_EXPORT_STRATEGY:
                return strategies.get(ExportConstants.ONLY_TASK_EXPORT_STRATEGY);
            case ExportConstants.TASK_WITH_JOURNAL_EXPORT_STRATEGY:
                return strategies.get(ExportConstants.TASK_WITH_JOURNAL_EXPORT_STRATEGY);
            default:
                return null;
        }
    }
}
