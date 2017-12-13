package server.controller;

import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;
import server.properties.ParserProperties;

import javax.swing.*;
import java.io.*;
import java.util.Date;


public class XmlSerializationMain {
    public static void main(String[] args) {
        Task task1 = new Task("1", TaskStatus.Cancelled, "11", new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()), 0);
        Task task2 = new Task("2", TaskStatus.Cancelled, "22", new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()), 1);

        Task task3 = new Task("3", TaskStatus.Cancelled, "33", new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()), 2);

        Journal j = new Journal();
        j.addTask(task1);
        j.addTask(task2);
        j.addTask(task3);

        XMLSerializer serializer = new XMLSerializer();

        try {
            serializer.writeJournal(j, ParserProperties.getInstance().getProperties("XML_FILE"));
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Incorrect journal in file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            Journal journal = serializer.readJournal(ParserProperties.getInstance().getProperties("XML_FILE"));
            System.out.println(journal);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Incorrect journal in file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}