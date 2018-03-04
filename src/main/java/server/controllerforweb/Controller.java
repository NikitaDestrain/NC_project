package server.controllerforweb;

import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLJournalDAO;
import database.postgresql.PostgreSQLTasksDAO;
import database.postgresql.PostgreSQLUsersDAO;
import server.controller.LifecycleManager;
import server.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;

public class Controller {
    private JournalContainer journalContainer;
    private UserContainer userContainer;
    private JournalNamesContainer journalNamesContainer;
    private LifecycleManager statusManager;
    private PostgreSQLDAOFactory postgreSQLDAOFactory;
    private PostgreSQLUsersDAO usersDAO;
    private PostgreSQLJournalDAO journalDAO;
    private PostgreSQLTasksDAO tasksDAO;
    private XmlUtils xmlUtils;
    private UserAuthorizer userAuthorizer;
    private static Controller instance;

    //todo статусы у задачи
    private Controller() {
        journalNamesContainer = new JournalNamesContainer();
        postgreSQLDAOFactory = PostgreSQLDAOFactory.getInstance();
        statusManager = LifecycleManager.getInstance();
        usersDAO = (PostgreSQLUsersDAO) postgreSQLDAOFactory.getUsersDao();
        journalDAO = (PostgreSQLJournalDAO) postgreSQLDAOFactory.getJournalDao();
        tasksDAO = (PostgreSQLTasksDAO) postgreSQLDAOFactory.getTasksDao();
        userAuthorizer = UserAuthorizer.getInstance();
        xmlUtils = XmlUtils.getInstance();
        //todo создать классы исклдючений для некорректного заполнения контейнера
        try {
            createUserContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            createJournalContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Controller getInstance() {
        if (instance == null) instance = new Controller();
        return instance;
    }

    private boolean checkDate(Date date) {
        return !date.before(Calendar.getInstance().getTime());
    }

    public void addUser(String login, String password, String role) throws SQLException {
        if (userAuthorizer.isSuchLoginExists(login)) {
            User user = usersDAO.create(login, password, role);
            userContainer.addUser(user);
            userAuthorizer.addUser(user);
        }
    }

    public void deleteUser(int id) throws SQLException {
        usersDAO.delete(id);
        userAuthorizer.removeUser(userContainer.getUser(id).getLogin());
        userContainer.removeUser(id);
    }

    public void editUser(User user) throws SQLException {
        usersDAO.update(user);
    }

    public String getUser(int id) {
        try {
            File file = new File("data/user.xml");
            JAXBContext context = JAXBContext.newInstance(User.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userContainer.getUser(id), file);
            return xmlUtils.parseXmlToString("data/user.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsers() {
        try {
            File file = new File("data/users.xml");
            JAXBContext context = JAXBContext.newInstance(UserContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userContainer, file);
            return xmlUtils.parseXmlToString("data/users.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editUserRole(int id, String role) throws SQLException {
        userContainer.setRole(id, role);
        usersDAO.update(userContainer.getUser(id));
    }

    public void addTask(String name, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        journalContainer.getJournal(journalId).addTask(tasksDAO.create(name, TaskStatus.Planned, description, notificationDate,
                plannedDate, journalId));
    }

    public void deleteTask(Task task) throws SQLException {
        tasksDAO.delete(task.getId());
        journalContainer.getJournal(task.getJournalId()).removeTask(task.getId());
    }

    public void editTask(Task task) throws SQLException {
        tasksDAO.update(task);
    }

    public String getTask(int journalId, int id) {
        try {
            File file = new File("data/task.xml");
            JAXBContext context = JAXBContext.newInstance(Task.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer.getJournal(journalId).getTask(id), file);
            return xmlUtils.parseXmlToString("data/task.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTasks(int journalId) {
        try {
            File file = new File("data/tasks.xml");
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer.getJournal(journalId), file);
            return xmlUtils.parseXmlToString("data/tasks.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedTasks(String column, String criteria) throws SQLException {
        List<Task> sortedTasks = null;
        if (criteria.equalsIgnoreCase("asc"))
            sortedTasks = tasksDAO.getSortedByAscending(column);
        if (criteria.equalsIgnoreCase("desc"))
            sortedTasks = tasksDAO.getSortedByDescending(column);
        Journal sortedTasksJournal = new Journal();
        if (sortedTasks != null)
            for (Task task : sortedTasks)
                sortedTasksJournal.addTask(task);
        try {
            File file = new File("data/sortedTasks.xml");
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(sortedTasksJournal, file);
            return xmlUtils.parseXmlToString("data/sortedTasks.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addJournal(String name, String description, Integer userId) throws SQLException {
        journalContainer.addJournal(journalDAO.create(name, description, userId));
        journalNamesContainer.addName(name);
    }

    public void deleteJournal(int id) throws SQLException {
        journalDAO.delete(id);
        journalNamesContainer.deleteName(journalContainer.getJournal(id).getName());
        journalContainer.removeJournal(id);
    }

    public void editJournal(Journal journal) throws SQLException {
        journalDAO.update(journal);
        //todo editName у namesContainer
    }

    public String getJournal(int id) {
        try {
            File file = new File("data/journal.xml");
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer.getJournal(id), file);
            return xmlUtils.parseXmlToString("data/journal.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJournals() {
        try {
            File file = new File("data/journals.xml");
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer, file);
            return xmlUtils.parseXmlToString("data/journals.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedJournals(String column, String criteria) throws SQLException {
        List<Journal> sortedJournals = null;
        if (criteria.equalsIgnoreCase("asc"))
            sortedJournals = journalDAO.getSortedByAscending(column);
        if (criteria.equalsIgnoreCase("desc"))
            sortedJournals = journalDAO.getSortedByDescending(column);
        JournalContainer sortedJournalContainer = new JournalContainer();
        if (sortedJournals != null)
            for (Journal journal : sortedJournals)
                sortedJournalContainer.addJournal(journal);
        try {
            File file = new File("data/sortedJournals.xml");
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(sortedJournalContainer, file);
            return xmlUtils.parseXmlToString("data/sortedJournals.xml");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createUserContainer() throws SQLException {
        userContainer = new UserContainer();
        for (User user : usersDAO.getAll())
            userContainer.addUser(user);
    }

    private void createJournalContainer() throws SQLException {
        journalContainer = new JournalContainer();
        for (Journal journal : journalDAO.getAll())
            journalContainer.addJournal(journal);
    }
}
