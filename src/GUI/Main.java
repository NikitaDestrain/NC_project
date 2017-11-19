package GUI;

import GUI.MainForm.MainForm;
import GUI.NotificationWindow.NotificationForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainForm());
    }
}
