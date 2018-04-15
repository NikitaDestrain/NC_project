package server.importdata.journalstrategy;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.importdata.StoreException;
import server.importdata.StoreStrategy;
import server.model.Journal;

public class IgnoreJournalStrategy<T> implements StoreStrategy<T> {
    private Controller controller;

    @Override
    public Object store(T object) throws StoreException {
        try {
            controller = Controller.getInstance();
            Journal j = (Journal) object;
            if (!controller.containsObject(j) && !controller.isExistId(j.getId())) {
                return controller.addJournal(j);
            }
        } catch (ControllerActionException e) {
            throw new StoreException(e.getMessage());
        }
        return null;
    }
}
