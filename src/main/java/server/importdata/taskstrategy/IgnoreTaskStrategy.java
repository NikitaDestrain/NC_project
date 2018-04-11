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
    private Utils utils = Utils.getInstance();

    @Override
    public boolean store(T object) throws StoreException {
        try {
            controller = Controller.getInstance();
            Task t = (Task) object;
            if (!controller.containsObject(t) && !controller.isExistId(t.getId())) {
                if (t.getNotificationDate() == null && t.getPlannedDate() == null &&
                        t.getUploadDate() == null && t.getChangeDate() == null) {
                    t.setNotificationDate(Date.valueOf(utils.reverseDate(t.getNotification())));
                    t.setPlannedDate(Date.valueOf(utils.reverseDate(t.getPlanned())));
                    t.setChangeDate(Date.valueOf(utils.reverseDate(t.getChange())));
                    t.setUploadDate(Date.valueOf(utils.reverseDate(t.getUpload())));
                    controller.addTask(t);
                    return true;
                }
                else {
                    controller.addTask(t);
                    return true;
                }
            }
        } catch (ControllerActionException e) {
            throw new StoreException(e.getMessage());
        }
        return false;
    }
}
