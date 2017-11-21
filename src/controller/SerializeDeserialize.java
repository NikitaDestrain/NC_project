package controller;

import model.Journal;
import model.Task;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SerializeDeserialize implements Serializer {

    @Override
    public void writeJournal(Journal journal) throws IOException {
        //todo all work with streams must be encapsulated in Serializer
        FileOutputStream fos = new FileOutputStream("backup.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
        try {
            List<Task> tasks = journal.getTasks();
            int size = tasks.size();
            for (int i = 0; i < size; i++) {
                objectOutputStream.writeObject(tasks.get(i));//todo why not save the whole journal?
            } // ПОГУГЛИТЬ КАК ЗАПИСАТЬ ЦЕЛИКОМ ЖУРНАЛ С ЛИСТОМ
        }
        catch (IOException e) {
            System.out.println("Не записался");
        }
    }

    @Override
    public Journal readJournal() throws IOException {
        FileInputStream fis = new FileInputStream("backup.txt");
        Journal journal = null;
        ObjectInputStream objectInputStream = new ObjectInputStream(fis);
        List<Task> tasks = new LinkedList<>();
        try {
            Object obj;
            while((obj = objectInputStream.readObject()) != null) {
                tasks.add((Task) obj);
            }
            journal = new Journal(tasks);
        }
        catch (EOFException e) {
            journal = new Journal(tasks);// ПРЕЕДЕЛАТЬ
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return journal;
    }
}
