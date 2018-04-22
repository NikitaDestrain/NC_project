package server.exportdata.exporttask;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.exportdata.ExportException;
import server.exportdata.ExportList;
import server.exportdata.ExportStrategy;
import server.model.Task;

public class TaskWithJournalExportStrategy implements ExportStrategy {

    private Controller controller;

    @Override
    public void collectId(ExportList exportList, Integer id) throws ExportException {
        try {
            controller = Controller.getInstance();
            if (controller.containsId(id)) {
                exportList.addTaskId(id);
                Task exportTask = controller.getTask(id);
                exportList.addJournalId(exportTask.getJournalId());
            } else throw new ExportException();
        } catch (ControllerActionException e) {
            throw new ExportException(e.getMessage());
        }
    }
}
