package server.exportdata;

public interface ExportStrategy {
    ExportList collectId(ExportList exportList, Integer id) throws ExportException;
}
