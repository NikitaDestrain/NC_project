package server.exportdata;

import server.exportdata.config.ExportConfigItem;
import server.exportdata.exportjournal.JournalWithTasksExportStrategy;
import server.exportdata.exportjournal.OnlyJournalExportStrategy;
import server.exportdata.exporttask.OnlyTaskExportStrategy;
import server.exportdata.exporttask.TaskWithJournalExportStrategy;

import java.util.HashMap;
import java.util.Map;

public class ExportStrategyHelper {
    private static ExportStrategyHelper ourInstance;
    private Map<String, Map<String, ExportStrategy>> strategies;

    public static ExportStrategyHelper getInstance() {
        if (ourInstance == null) ourInstance = new ExportStrategyHelper();
        return ourInstance;
    }

    private ExportStrategyHelper() {
        strategies = new HashMap<>();

        Map<String, ExportStrategy> journalStrategies = new HashMap<>();
        journalStrategies.put(ExportConstants.ONLY_OBJECT, new OnlyJournalExportStrategy());
        journalStrategies.put(ExportConstants.OBJECT_WITH_CHILDREN, new JournalWithTasksExportStrategy());
        strategies.put(ExportConstants.JOURNAL_PROPERTY, journalStrategies);

        Map<String, ExportStrategy> taskStrategies = new HashMap<>();
        taskStrategies.put(ExportConstants.ONLY_OBJECT, new OnlyTaskExportStrategy());
        taskStrategies.put(ExportConstants.OBJECT_WITH_PARENT, new TaskWithJournalExportStrategy());
        strategies.put(ExportConstants.TASK_PROPERTY, taskStrategies);
    }

    public ExportStrategy resolveStrategy(ExportConfigItem configItem) {
        return strategies.get(configItem.getType()).get(configItem.getStrategy());

        //todo vlla весь этот код можно заменить одной строчкой (о нездоровой любви к switch) DONE
    }
}
