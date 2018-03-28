package server.exportdata;

public interface ExportStrategy {
    ExportList collectId(ExportList exportList) throws ExportException;
}
