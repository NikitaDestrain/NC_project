package controller;

import model.Journal;
import model.Task;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SerializeDeserialize implements Serializer {

    @Override
    public void writeJournal(Journal journal, OutputStream out) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        try {
            List<Task> tasks = journal.getTasks();
            int size = tasks.size();
            for (int i = 0; i < size; i++) {
                objectOutputStream.writeObject(tasks.get(i));
            }
        }
        catch (IOException e) {
            System.out.println("Не записался");
        }
    }

    @Override
    public Journal readJournal(InputStream in) throws IOException {
        Journal journal = null;
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        List<Task> tasks = new LinkedList<>();
        try {
            Object obj;
            while((obj = objectInputStream.readObject()) != null) {
                tasks.add((Task) obj);
            }
            journal = new Journal(tasks);
        }
        catch (EOFException e) {
            journal = new Journal(tasks);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return journal;
    }
}
