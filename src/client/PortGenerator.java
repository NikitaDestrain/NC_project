package client;

public class PortGenerator {
    private int nextPort;
    private static PortGenerator instance;

    private PortGenerator(int nextPort){
        this.nextPort = nextPort;
    }

    public static PortGenerator getInstance(){
        if (instance == null)
            instance = new PortGenerator(1000);
        return instance;
    }

    public synchronized int createPort() {
        return ++nextPort;
    }
}
