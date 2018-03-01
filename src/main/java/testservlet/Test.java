package testservlet;

import server.factories.TaskFactory;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws JAXBException, IOException {
        Task task1 = new Task("1", TaskStatus.Overdue, "desc1", new Date(), new Date(),
                new Date(), new Date(), 0, 0);
        Task task2 = new Task("2", TaskStatus.Overdue, "desc2", new Date(), new Date(),
                new Date(), new Date(), 1, 1);
        Journal journal = new Journal();
        journal.addTask(task1);
        journal.addTask(task2);

        JAXBContext context = JAXBContext.newInstance(Journal.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(journal, new File("xsd/journal.xml"));
    }
}
