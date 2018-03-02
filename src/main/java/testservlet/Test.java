package testservlet;

import server.factories.TaskFactory;
import server.model.Journal;
import server.model.JournalContainer;
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
        Task task3 = new Task("3", TaskStatus.Overdue, "desc3", new Date(), new Date(),
                new Date(), new Date(), 1, 1);
        Journal journal1 = new Journal();
        journal1.addTask(task1);
        journal1.addTask(task2);
        journal1.addTask(task3);
        journal1.setName("j1");

        Journal journal2 = new Journal();
        journal2.addTask(task2);
        journal2.addTask(task1);
        journal2.setDescription("j1Desc");

        JournalContainer container = new JournalContainer();
        container.addJournal(journal1);
        container.addJournal(journal2);

        JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(container, new File("xsd/journals.xml"));
    }
}
