package client;

import client.gui.authforms.AuthForm;
import client.gui.mainform.MainForm;
import client.network.ClientNetworkFacade;

import javax.swing.*;

public class StartClient {

    public static void main(String[] args) {
        ClientNetworkFacade cnf = ClientNetworkFacade.getInstance();
        cnf.start();
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }
}
