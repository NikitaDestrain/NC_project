package server.exportdata;

import server.exportdata.config.ExportConfigHelper;
import server.exportdata.config.ExportConfigItem;

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

    private void fillExportListByType(ExportList exportList, String type, List<Integer> IDs) throws ExportException {
        if (IDs == null)
            return;

        ExportConfigItem configItem = configHelper.getStrategy(type);
        ExportStrategy exportStrategy = strategyHelper.resolveStrategy(configItem);
        for (Integer id : IDs) {
            exportStrategy.collectId(exportList, id);
        }

        //todo vlla вот любите вы switch нездоровой любовью
        // этот код можно уменьшить в два раза, сделав при этом его более гибким
        // как вы думаете, зачем я вообще ввел этот метод в своем коде? Просто из непреодолимого желаения написать как можно больше методов?
        // Ответ: я ввел его, чтобы сделать более повторно используемый код.

    }
}
