package server.importdata.journalstrategy;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.importdata.StoreConstants;
import server.importdata.StoreException;
import server.importdata.StoreStrategy;
import server.model.Journal;

public class ExceptionThrowJournalStrategy<T> implements StoreStrategy<T> {
    private Controller controller;

    @Override
    public void store(T object) throws StoreException {
        try {
            controller = Controller.getInstance();
            Journal j = (Journal) object;
            if (!controller.containsObject(j) && !controller.containsId(j.getId())) {
                controller.addJournal(j);
            } else {
                throw new StoreException(StoreConstants.STORE_EXCEPTION_MESSAGE + j.getId());
            }
        } catch (ControllerActionException e) {
            throw new StoreException(e.getMessage());
        }
    }
}
