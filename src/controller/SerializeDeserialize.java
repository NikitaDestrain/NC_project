package controller;

import model.Journal;
import model.Task;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SerializeDeserialize implements Serializer {

    @Override
    public void writeJournal(Journal journal) throws IOException {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("backup.txt"))) {
            objectOutputStream.writeObject(journal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Journal readJournal() throws IOException {
        Journal journal = null;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("backup.txt"))) {
            journal = (Journal) objectInputStream.readObject();
        } catch (EOFException e) {
            return null;
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return journal;
    }

}
