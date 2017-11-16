package controller;

import model.Journal;

import java.io.*;

public class SerializeDeserialize implements Serializer {

    @Override
    public void writeJournal(Journal journal, OutputStream out) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        try {
            objectOutputStream.writeObject(journal);
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    @Override
    public Journal readJournal(InputStream in) throws IOException {
        Journal journal = null;
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        try {
            journal = (Journal)objectInputStream.readObject();
        }
        catch (IOException e) {
            e.getMessage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return journal;
    }
}
