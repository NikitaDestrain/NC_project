package server.beans;

import server.exportdata.ExportException;
import server.importdata.StoreException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface EIBeanLocal {
    String exportData(List<Integer> journalIDs, List<Integer> taskIDs) throws ExportException;
    void importData(String xml, String journalStrategy, String taskStrategy) throws StoreException;
}
