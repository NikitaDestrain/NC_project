package server.network;

/**
 * Generate unique port for client's notification channel
 */

public class PortGenerator {
    private int nextPort;
    private static PortGenerator instance;

    private PortGenerator(int nextPort) {
        this.nextPort = nextPort;
    }

    public synchronized static PortGenerator getInstance() { //todo vlla synchronized DONE
        if (instance == null)
            instance = new PortGenerator(1000);
        return instance;
    }

    /**
     * Creates unique port
     *
     * @return unique port
     */

    public synchronized int createPort() {
        return ++nextPort;
    }
}
