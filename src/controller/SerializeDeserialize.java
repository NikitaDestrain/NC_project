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
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("backup.txt"))) {
//            List<Task> tasks = journal.getTasks();
//            int size = tasks.size();
//            for (int i = 0; i < size; i++) {
//                objectOutputStream.writeObject(tasks.get(i));//todo why not save the whole journal?
//            } // ПОГУГЛИТЬ КАК ЗАПИСАТЬ ЦЕЛИКОМ ЖУРНАЛ С ЛИСТОМ
            objectOutputStream.writeObject(journal);
        } catch (IOException e) {
            System.out.println("Не записался");
        }
    }

    @Override
    public Journal readJournal() throws IOException {
        Journal journal = null;
        //List<Task> tasks = new LinkedList<>();
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("backup.txt"))) {
//            Object obj;
//            while ((obj = objectInputStream.readObject()) != null) {
//                tasks.add((Task) obj);
//            }
            //journal = new Journal(tasks);
            journal = (Journal) objectInputStream.readObject();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (EOFException e) {
            //journal = new Journal(tasks);// ПРЕЕДЕЛАТЬ
        }
        return journal;
    }

}
