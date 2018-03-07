package servlet;

import auxiliaryclasses.ConstantsClass;
import server.controllerforweb.XmlUtils;
import server.model.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws JAXBException, IOException {
//        Task task1 = new Task(0, "1", TaskStatus.Overdue, "desc1", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
//                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 0);
//        Task task2 = new Task(1, "2", TaskStatus.Overdue, "desc2", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
//                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 1);
//        Task task3 = new Task(2, "3", TaskStatus.Overdue, "desc3", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
//                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 1);
//        Journal journal1 = new Journal();
//        journal1.addTask(task1);
//        journal1.addTask(task2);
//        journal1.addTask(task3);
//        journal1.setName("j1");
//        journal1.setId(0);
//
//        Journal journal2 = new Journal();
//        journal2.addTask(task2);
//        journal2.addTask(task1);
//        journal2.setName("j2");
//        journal2.setDescription("j1Desc");
//        journal2.setId(1);
//
//        JournalContainer container = new JournalContainer();
//        container.addJournal(journal1);
//        container.addJournal(journal2);

//        List<String> names = new LinkedList<>();
//
//        for (Journal j : container.getJournals()) {
//            names.add(j.getName());
//        }
//        JournalNamesContainer namesContainer = new JournalNamesContainer(names);
//
//        System.out.println();
//
//        try {
//            XmlUtils.getInstance().writeNames(namesContainer, "data/names.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("done");
//
//        System.out.println(XmlUtils.getInstance().compareWithXsd(
//                "data/names.xml",
//                "data/names.xsd"
//        ));
//
//        System.out.println(XmlUtils.getInstance().parseXmlToString("data/names.xml"));

//        try {
//            XmlUtils.getInstance().writeJournal(journal1, "data/journal.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(XmlUtils.getInstance().compareWithXsd("data/journal.xml", "data/journal.xsd"));
//        try {
//            XmlUtils.getInstance().writeJournal(new Journal("name", "desc"), "data/journal.xml");
//            System.out.println(XmlUtils.getInstance().compareWithXsd("data/journal.xml", "data/journal.xsd"));
//        } catch (Exception e) {
//            e.getMessage();
//        }

        System.out.println(isLikeFilterCorrect(""));
    }

    private static boolean isLikeFilterCorrect(String like) {
        Pattern pattern = Pattern.compile("[%_]*[A-Za-z0-9]+[%_]*");
        Matcher matcher = pattern.matcher(like);

        return matcher.matches();
    }
}
