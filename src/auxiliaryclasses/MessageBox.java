package auxiliaryclasses;

import client.network.ClientNetworkFacade;

import javax.swing.*;

public class MessageBox {
    private static MessageBox instance;
    private static ClientNetworkFacade clientFacade;

    private MessageBox() {
    }

    public static synchronized MessageBox getInstance() {
        if (instance == null) {
            instance = new MessageBox();
            clientFacade = ClientNetworkFacade.getInstance();
        }
        return instance;
    }

    public synchronized void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error!", JOptionPane.ERROR_MESSAGE);
    }

    public synchronized void showAskForRestartMessage() {
        showMessage(ConstantsClass.UNSUCCESSFUL_CONNECTION);
        clientFacade.finish();
        System.exit(0);
    }
}
