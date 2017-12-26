package server.gui.taskwindow;


import server.controller.Controller;
import server.controller.IDGenerator;
import server.gui.mainform.MainForm;
import server.gui.authforms.AuthForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //IDGenerator.getInstance(Controller.getInstance().getJournal().getMaxId());
            new AuthForm().setVisible(true);
        });
    }
}
