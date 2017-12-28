package server;

import server.gui.authforms.AuthForm;
import server.gui.mainform.MainForm;
import server.network.ServerNetworkFacade;

import javax.swing.*;

public class StartServer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }
}
