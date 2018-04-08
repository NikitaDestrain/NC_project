package server.importdata;

public interface StoreStrategy<T> {
    void store(T object) throws StoreException;
}
