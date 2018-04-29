package server.importdata;

public interface StoreStrategy<T> {
    Object store(T object) throws StoreException;
}
