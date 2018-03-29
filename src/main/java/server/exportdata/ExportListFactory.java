package server.exportdata;

import com.sun.xml.internal.bind.v2.model.core.ID;
import server.exportdata.config.ExportConfigHelper;

import java.util.LinkedList;
import java.util.List;

public class ExportListFactory {
    private static ExportListFactory ourInstance = new ExportListFactory();
    private ExportStrategyHelper strategyHelper = ExportStrategyHelper.getInstance();
    private ExportConfigHelper configHelper = ExportConfigHelper.getInstance();

    private final String EXPORT_ERROR = "Could not perform export! Select journals or tasks!";

    public static ExportListFactory getInstance() {
        return ourInstance;
    }

    private ExportListFactory() {
    }

    public ExportList createList(List<Integer> journalIDs, List<Integer> taskIDs) throws ExportException {
        ExportList exportList = new ExportList();

        fillExportListByType(exportList, ExportConstants.JOURNAL_PROPERTY, journalIDs);
        fillExportListByType(exportList, ExportConstants.TASK_PROPERTY, taskIDs);

        return exportList;
    }

    private void fillExportListByType(ExportList exportList, String strategy, List<Integer> IDs) throws ExportException {
        if (IDs == null || IDs.size() == 0) {
            throw new ExportException(ExportConstants.INCORRECT_PARAMETER);
        } else {
            int strategyType;
            ExportStrategy exportStrategy;
            switch (strategy) {
                case ExportConstants.JOURNAL_PROPERTY:
                    strategyType = configHelper.getJournalStrategy();
                    exportStrategy = strategyHelper.resolveStrategy(strategyType);
                    for (Integer id : IDs) {
                        exportStrategy.collectId(exportList, id);
                    }
                    break;
                case ExportConstants.TASK_PROPERTY:
                    strategyType = configHelper.getTaskStrategy();
                    exportStrategy = strategyHelper.resolveStrategy(strategyType);
                    for (Integer id : IDs) {
                        exportStrategy.collectId(exportList, id);
                    }
                    break;
            }
        }
    }
}
