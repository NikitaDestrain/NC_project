package server.exportdata.exportjournal;

import server.controller.Controller;
import server.exceptions.ControllerActionException;
import server.exportdata.ExportException;
import server.exportdata.ExportList;
import server.exportdata.ExportStrategy;
import server.model.Journal;
import server.model.Task;

public class JournalWithTasksExportStrategy implements ExportStrategy {

    private Controller controller;

    @Override
    public void collectId(ExportList exportList, Integer id) throws ExportException {
        try {
            controller = Controller.getInstance();
            if (controller.containsId(id)) {
                Journal exportJournal = controller.getJournal(id);
                exportList.addJournalId(id);
                for (Task exportTask : exportJournal.getTasks())
                    exportList.addTaskId(exportTask.getId());
            } else throw new ExportException();
        } catch (ControllerActionException e) {
            throw new ExportException();
        }
    }
}
