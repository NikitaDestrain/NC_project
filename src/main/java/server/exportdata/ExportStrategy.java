package server.exportdata;

public interface ExportStrategy {
    void collectId(ExportList exportList, Integer id) throws ExportException;
}
