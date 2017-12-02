package controller;

import gui.mainform.MainForm;
import model.Journal;
import model.Task;
import properties.ParserProperties;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SerializeDeserialize implements Serializer {

    @Override
    public void writeJournal(Journal journal) throws IOException {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(ParserProperties.getProperties("PATH_TO_JOURNAL")))) {
            objectOutputStream.writeObject(journal);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not save journal to file ",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Journal readJournal() throws IOException {
        Journal journal = null;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(ParserProperties.getProperties("PATH_TO_JOURNAL")))) {
            journal = (Journal) objectInputStream.readObject();
        } catch (EOFException e) {
            return null;
        } catch (ClassNotFoundException e1) {
            return null;
        }
        return journal;
    }

}
