package server.controller;

import server.model.Journal;

import java.io.*;

public class ObjectSerializer implements Serializer {

    /**
     * Writes the received journal to the file with specified path
     * @param journal received
     * @param path where the journal will be written
     * @throws IOException
     */
    @Override
    public void writeJournal(Journal journal, String path) throws Exception {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(path));
            objectOutputStream.writeObject(journal);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Reads the journal from the file with specified path
     * @param path where the journal should be read
     * @return {@code Journal} object if reading was correct and null otherwise
     * @throws IOException
     */

    @Override
    public Journal readJournal(String path) throws Exception {
        Journal journal;
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
