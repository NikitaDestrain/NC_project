package server.importdata.journalstrategy;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.importdata.StoreException;
import server.importdata.StoreStrategy;
import server.model.Journal;

public class ReplaceJournalStrategy<T> implements StoreStrategy<T> {
    private Controller controller;

    @Override
    public boolean store(T object) throws StoreException {
        try {
            controller = Controller.getInstance();
            Journal j = (Journal) object;
            if (controller.containsObject(j)) {
                controller.editJournal(j.getId(), j.getName(), j.getDescription());
                return true;
            } else if (!controller.isExistId(j.getId())){
                controller.addJournal(j);
                return true;
            }
        } catch (ControllerActionException e) {
            throw new StoreException(e.getMessage());
        }
        return false;
    }
}
