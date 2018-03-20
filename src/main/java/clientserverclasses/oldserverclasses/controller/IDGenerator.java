package clientserverclasses.oldserverclasses.controller;

public class IDGenerator {
    private int nextId;
    private static IDGenerator instance;

    private IDGenerator(int nextId) {
        this.nextId = nextId;
    }

    /**
     * Gets or creates the instance of current <code>IDGenerator</code> with specified nextId
     */

    public synchronized static IDGenerator getInstance(int nextId) {
        if (instance == null)
            instance = new IDGenerator(nextId);
        return instance;
    }

    /**
     * Gets or creates the instance of current <code>IDGenerator</code> with nextId = 0
     */

    public synchronized static IDGenerator getInstance() {
        if (instance == null)
            instance = new IDGenerator(0);
        return instance;
    }

    /**
     * Increments the <code>nextId</code> field
     *
     * @return
     */

    public synchronized int createId() {
        return ++nextId;
    }
}
