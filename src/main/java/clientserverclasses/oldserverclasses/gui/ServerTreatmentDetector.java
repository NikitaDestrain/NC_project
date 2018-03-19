package clientserverclasses.oldserverclasses.gui;

public class ServerTreatmentDetector {
    private static ServerTreatmentDetector ourInstance = new ServerTreatmentDetector();
    private int detector = 0;

    public static ServerTreatmentDetector getInstance() {
        return ourInstance;
    }

    private ServerTreatmentDetector() {
    }

    public void serverTreatment() {
        detector = 1;
    }

    public void clearTreatment() {
        detector = 0;
    }

    public synchronized int getDetector() {
        return detector;
    }
}
