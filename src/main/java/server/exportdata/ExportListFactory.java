package server.exportdata;

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
        if (journalIDs != null && journalIDs.size() > 0) {
            return strategyHelper.resolveStrategy(configHelper.getJournalStrategy(), new ExportList(journalIDs, new LinkedList<>()));
        } else if (taskIDs != null && taskIDs.size() > 0) {
            return strategyHelper.resolveStrategy(configHelper.getTaskStrategy(), new ExportList(new LinkedList<>(), taskIDs));
        } else {
            throw new ExportException(EXPORT_ERROR);
        }
    }
}
