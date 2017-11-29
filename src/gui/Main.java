package gui;

import controller.Controller;
import controller.IDGenerator;
import gui.mainform.MainForm;
import properties.Prop;
import controller.SerializeDeserialize;
import model.Journal;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {
        try {
            Prop prop = new Prop(); //поля этого объекта содержат необхдимые значения
        } catch (IOException e) {
            System.out.println(e.toString()+" не верно указан файл конфигурации");
        }
        SwingUtilities.invokeLater(() -> {
            try {
                Journal journal = new SerializeDeserialize().readJournal();
                if (journal == null)
                    JOptionPane.showMessageDialog(null, "Incorrect journal in file. You may create a new one","Error", JOptionPane.ERROR_MESSAGE);
                IDGenerator.getInstance(journal.getMaxId());
                new MainForm().setJournal(journal);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not load journal from file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
                IDGenerator.getInstance();
                new MainForm().setJournal(null);
            }
        });
    }
}
