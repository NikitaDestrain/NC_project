package GUI;

import GUI.MainForm.MainForm;
import GUI.NotificationWindow.NotificationForm;
import controller.SerializeDeserialize;
import model.Journal;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException { // todo свернуть в трей
        // todo иниц файл, иниц генератор на журнал
        Journal journal = new SerializeDeserialize().readJournal();
        SwingUtilities.invokeLater(() -> new MainForm().setJournal(null));
        // todo config file ключ - значение
    }
}
