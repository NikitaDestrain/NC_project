package server.exportdata.exporttask;

import server.exportdata.ExportException;
import server.exportdata.ExportList;
import server.exportdata.ExportStrategy;

public class TaskWithJournalExportStrategy implements ExportStrategy {
    @Override
    public ExportList collectId(ExportList exportList, Integer id) throws ExportException {
        return null;
    }
}
