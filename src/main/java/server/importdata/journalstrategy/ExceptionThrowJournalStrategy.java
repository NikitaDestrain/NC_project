package server.importdata.journalstrategy;

import server.importdata.StoreException;
import server.importdata.StoreStrategy;

public class ExceptionThrowJournalStrategy<T> implements StoreStrategy<T> {
    @Override
    public boolean store(T object) throws StoreException {
        return true;
    }
}
