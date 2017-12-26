package server.network;

import server.gui.mainform.MainForm;

import javax.swing.*;

public class StartServer {

    public static void main(String[] args) {
        ServerNetworkFacade snf = ServerNetworkFacade.getInstance();
        snf.start();
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}
