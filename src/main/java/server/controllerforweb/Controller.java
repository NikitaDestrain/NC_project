package server.controllerforweb;

import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLJournalDAO;
import database.postgresql.PostgreSQLTasksDAO;
import database.postgresql.PostgreSQLUsersDAO;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import server.controller.LifecycleManager;
import server.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;

//todo возвращать XML (проверить тестовый вариант)
public class Controller {
    private JournalContainer journalContainer;
    private UserContainer userContainer;
    private LifecycleManager statusManager;
    private PostgreSQLDAOFactory postgreSQLDAOFactory;
    private PostgreSQLUsersDAO usersDAO;
    private PostgreSQLJournalDAO journalDAO;
    private PostgreSQLTasksDAO tasksDAO;
    private static Controller instance;

    private Controller() {
        journalContainer = new JournalContainer();
        userContainer = new UserContainer();
        postgreSQLDAOFactory = PostgreSQLDAOFactory.getInstance();
        statusManager = LifecycleManager.getInstance();
        usersDAO = (PostgreSQLUsersDAO) postgreSQLDAOFactory.getUsersDao();
        journalDAO = (PostgreSQLJournalDAO) postgreSQLDAOFactory.getJournalDao();
        tasksDAO = (PostgreSQLTasksDAO) postgreSQLDAOFactory.getTasksDao();
    }

    public static synchronized Controller getInstance() {
        if (instance == null) instance = new Controller();
        return instance;
    }

    private boolean checkDate(Date date) {
        return !date.before(Calendar.getInstance().getTime());
    }

    //users

    //вызывать после проверки через UserAuthorizer
    public void addUser(String login, String password, String role) throws SQLException {
        userContainer.addUser(usersDAO.create(login, password, role));
    }

    public void deleteUser(int id) throws SQLException {
        usersDAO.delete(id);
        userContainer.removeUser(id);
    }

    //сначала юзер меняется в модели, потом просто отправляется сюда и происходит апдейт на основе данных из модели
    public void editUser(User user) throws SQLException {
        usersDAO.update(user);
    }

    public User getUser(int id) {
        return userContainer.getUser(id);
    }

    //todo протестировать этот метод
    public Document getUsers() {
        try {
            //todo найти способ не создавать файл
            File file = new File("tmpUsers.xml");
            JAXBContext context = JAXBContext.newInstance(UserContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userContainer, file);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return documentBuilder.parse(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editUserRole(int id, String role) {
        userContainer.setRole(id, role);
    }


    //tasks

    public void addTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        journalContainer.getJournal(journalId).addTask(tasksDAO.create(name, status, description, notificationDate,
                plannedDate, journalId));
    }

    public void deleteTask(Task task) throws SQLException {
        tasksDAO.delete(task.getId());
        journalContainer.getJournal(task.getJournalId());
    }

    public void editTask(Task task) throws SQLException {
        tasksDAO.update(task);
    }

    //todo доделать после теста
    public Task getTask(int id) {
        return null;
    }

    public Document getTasks() {
        return null;
    }

    //journals

    public void addJournal(String name, String description, Integer userId) throws SQLException {
        journalContainer.addJournal(journalDAO.create(name, description, userId));
    }

    public void deleteJournal(int id) throws SQLException {
        journalDAO.delete(id);
        journalContainer.removeJournal(id);
    }

    public void editJournal(Journal journal) throws SQLException {
        journalDAO.update(journal);
    }

    public Journal getJournal(int id) {
        return journalContainer.getJournal(id);
    }

    //todo добавить реализацию после теста для юзеров
    public Document getJournals() {
        return null;
    }

}
