package server.exportdata.exportjournal;

import server.exportdata.ExportException;
import server.exportdata.ExportList;
import server.exportdata.ExportStrategy;

public class OnlyJournalExportStrategy implements ExportStrategy {
    @Override
    public ExportList collectId(ExportList exportList, Integer id) throws ExportException {
        return null;
    }
}
