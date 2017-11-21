package GUI;

import GUI.MainForm.MainForm;
import GUI.NotificationWindow.NotificationForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) { // todo свернуть в трей
        // todo иниц файл, иниц генератор на журнал
        SwingUtilities.invokeLater(() -> new MainForm());
        // todo config file ключ - значение
    }
}
