package server.importdata;

public interface StoreStrategy<T> {
    boolean store(T object) throws StoreException;
}
