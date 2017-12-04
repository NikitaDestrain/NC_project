package controller;

import model.Journal;

import java.io.*;

public class SerializeDeserialize implements Serializer {

    @Override
    public void writeJournal(Journal journal, String path) throws IOException {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(path));
            objectOutputStream.writeObject(journal);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Override
    public Journal readJournal(String path) throws IOException {
        Journal journal = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(path))) {
            journal = (Journal) objectInputStream.readObject();
        } catch (EOFException e) {
            return null;
        } catch (ClassNotFoundException e1) {
            return null;
        }
        return journal;
    }

}
