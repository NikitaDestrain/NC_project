package server.importdata.taskstrategy;

import server.importdata.StoreException;
import server.importdata.StoreStrategy;

public class IgnoreTaskStrategy<T> implements StoreStrategy<T> {
    @Override
    public boolean store(T object) throws StoreException {
        return true;
    }
}
