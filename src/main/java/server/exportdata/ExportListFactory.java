package server.exportdata;

import server.exportdata.config.ExportConfigHelper;
import server.exportdata.config.ExportConfigItem;

import java.util.List;

public class ExportListFactory {
    private static ExportListFactory ourInstance;
    private ExportStrategyHelper strategyHelper;
    private ExportConfigHelper configHelper;


    public static ExportListFactory getInstance() throws NumberFormatException {
        if (ourInstance == null) ourInstance = new ExportListFactory();
        return ourInstance;
    }

    private ExportListFactory() throws NumberFormatException {
        strategyHelper = ExportStrategyHelper.getInstance();
        configHelper = ExportConfigHelper.getInstance();
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

        //todo vlla вот любите вы switch нездоровой любовью DONE
        // этот код можно уменьшить в два раза, сделав при этом его более гибким
        // как вы думаете, зачем я вообще ввел этот метод в своем коде? Просто из непреодолимого желаения написать как можно больше методов?
        // Ответ: я ввел его, чтобы сделать более повторно используемый код.

    }
}
