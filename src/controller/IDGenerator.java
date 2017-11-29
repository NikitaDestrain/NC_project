package controller;

public class IDGenerator{

    private int nextId;
    private static IDGenerator instance;

    private IDGenerator(int nextId) {
        this.nextId = nextId;
    }

    public static IDGenerator getInstance(int nextId) {
        if (instance == null)
            instance = new IDGenerator(nextId);
        return instance;
    }

    public static IDGenerator getInstance() {
        if (instance == null)
            instance = new IDGenerator(0);
        return instance;
    }

    public synchronized int createId() {
        return ++nextId;
    }
}
