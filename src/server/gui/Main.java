package server.gui;

import server.controller.IDGenerator;
import server.exceptions.IllegalPropertyException;
import server.gui.mainform.MainForm;
import server.properties.ParserProperties;
import server.controller.SerializeDeserialize;
import server.model.Journal;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (ParserProperties.getInstance() == null) {

            JOptionPane.showMessageDialog(null, "Config file not found or damaged!", "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    Journal journal = new SerializeDeserialize().readJournal(ParserProperties.getInstance().getProperties("PATH_TO_JOURNAL"));//todo константы стоит выносить в public static final переменные
                    if (journal == null) {
                        IDGenerator.getInstance();
                        JOptionPane.showMessageDialog(null, "Incorrect journal in file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
                    } else
                        IDGenerator.getInstance(journal.getMaxId());
                    new MainForm().setJournal(journal);
                } catch (IllegalPropertyException ex) {
                    JOptionPane.showMessageDialog(null, "Illegal value of property",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Could not load journal from file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
                    IDGenerator.getInstance();
                    new MainForm().setJournal(null);
                }

            });
        }
    }
}
