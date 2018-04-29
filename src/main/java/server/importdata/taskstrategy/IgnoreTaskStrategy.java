package server.importdata.taskstrategy;

import server.controller.Controller;
import server.controller.Utils;
import server.exceptions.ControllerActionException;
import server.importdata.StoreException;
import server.importdata.StoreStrategy;
import server.model.Task;

import java.sql.Date;

public class IgnoreTaskStrategy<T> implements StoreStrategy<T> {
    private Controller controller;

    @Override
    public Object store(T object) throws StoreException {
        try {
            controller = Controller.getInstance();
            Task t = (Task) object;
            if (!controller.containsObject(t) && !controller.isExistId(t.getId())) {
                controller.addTask(t);
                return Boolean.TRUE;
            }
        } catch (ControllerActionException e) {
            throw new StoreException(e.getMessage());
        }
        return null;
    }
}
