package server.beans;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.exceptions.MarshallerException;
import server.exportdata.ExportException;
import server.exportdata.ExportList;
import server.exportdata.ExportListFactory;
import server.importdata.*;
import server.model.Journal;
import server.model.Task;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class ExportImportBean implements EIBeanLocal {

    private Marshaller marshaller = Marshaller.getInstance();
    private Controller controller;

    /**
     * 1. Config file parsing(Done in init-ion of AuthServlet because ServletContext.getRealPath is needed)
     * (In: file path; Out: <code>ExportConfigHelper</code> with parsed export strategies for tasks and journals)
     * 2. Filling in ExportList object using strategies.
     * (In: IDs of objects, what user has selected, config info;
     * Out: ExportList with received IDs).
     * 3. Getting objects from DB.
     * (In: Filled ExportList; Out: java objects from DB.)
     * 4. Transformation received objects to xml file
     * (In: java objects from DB; Out: xml-file)
     */

    @Override
    public String exportData(List<Integer> journalIDs, List<Integer> taskIDs) throws ExportException {
        try {
            controller = Controller.getInstance();

            ExportList exportList = ExportListFactory.getInstance().createList(journalIDs, taskIDs);
            List<Journal> journalList = controller.createJournalListByIds(exportList.getJournalIds());
            List<Task> taskList = controller.createTaskListByIds(exportList.getTaskIds());

            return marshaller.marshalToXMLString(journalList, taskList);
        } catch (ControllerActionException | MarshallerException e) {
            throw new ExportException();
        }
    }

    /**
     * 1. Parse received xml (In: xml; Out: parsed java object)
     * 2. Call <code>StoreStrategyHelper</code> to perform needed strategy and import received data into DB.
     * (In: strategy type, java object for import; Out: nothing, execution exception(if something gone wrong while
     * performing a strategy) or strategy exception)
     */

    @Override
    public void importData(String xml, String journalStrategy, String taskStrategy) throws StoreException {
        StoreItem storeItem = marshaller.unmarshal(xml);
        storeItem.setJournalStrategy(journalStrategy);
        storeItem.setTaskStrategy(taskStrategy);
        StoreStrategyHelper storeStrategyHelper = StoreStrategyHelper.getInstance();
        StoreStrategy<Journal> journalStoreStrategy = storeStrategyHelper.resolveStrategy(storeItem, StoreConstants.JOURNAL);
        StoreStrategy<Task> taskStoreStrategy = storeStrategyHelper.resolveStrategy(storeItem, StoreConstants.TASK);

        List<Journal> journals = storeItem.getJournals(); // todo лист тасок без журнала
        List<Task> tasks = storeItem.getTasks();
        if (journals.size() == 0) {
            for (Task t : tasks) {
                taskStoreStrategy.store(t);
            }
        } else {
            Object object;
            for (Journal j : journals) {
                object = journalStoreStrategy.store(j);
                if (object != null) {
                    for (Task t : j.getTasks()) {
                        if (object instanceof Journal) {
                            t.setJournalId(((Journal) object).getId());
                        } else if (object instanceof Boolean) {
                            taskStoreStrategy.store(t);
                        }
                    }
                }
            }
        }
    }
}
