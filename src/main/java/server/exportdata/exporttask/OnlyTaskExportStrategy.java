package server.exportdata.exporttask;

import server.exportdata.ExportException;
import server.exportdata.ExportList;
import server.exportdata.ExportStrategy;

public class OnlyTaskExportStrategy implements ExportStrategy {
    @Override
    public ExportList collectId(ExportList exportList) throws ExportException {
        return null;
    }
}
