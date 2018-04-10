package server.importdata.taskstrategy;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.importdata.StoreConstants;
import server.importdata.StoreException;
import server.importdata.StoreStrategy;
import server.model.Task;

public class ExceptionThrowTaskStrategy<T> implements StoreStrategy<T> {
    private Controller controller;

    @Override
    public void store(T object) throws StoreException {
        try {
            controller = Controller.getInstance();
            Task t = (Task) object;
            if (!controller.containsObject(t) && !controller.isExistId(t.getId())) {
                controller.addTask(t);
            } else {
                throw new StoreException(StoreConstants.STORE_EXCEPTION_MESSAGE + t.getId());
            }
        } catch (ControllerActionException e) {
            throw new StoreException(e.getMessage());
        }
    }
}
