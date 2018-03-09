package server.controller;

import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLJournalDAO;
import database.postgresql.PostgreSQLTasksDAO;
import database.postgresql.PostgreSQLUsersDAO;
import server.exceptions.ControllerActionException;
import server.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
    private Notifier notifier;
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
        notifier = new Notifier();
        //todo создать классы исклдючений для некорректного заполнения контейнера
        try {
            createUserContainer();
            createJournalContainer();
        } catch (ControllerActionException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Controller getInstance() {
        if (instance == null) instance = new Controller();
        return instance;
    }

    public User signInUser(String login, String password) {
        if (userAuthorizer.isUserDataCorrect(login, password))
            return userContainer.getUserByLogin(login);
        else return null;
    }

    public void addUser(String login, String password, String role) throws ControllerActionException {
        if (!userAuthorizer.isSuchLoginExists(login) && password != null) {
            try {
                User user = usersDAO.create(login, password, role);
                userContainer.addUser(user);
                userAuthorizer.addUser(user);
            } catch (SQLException e) {
                throw new ControllerActionException("Error! User has been not added.");
            }
        } else
            throw new ControllerActionException("Error! Login does not exist.");
    }

    public void deleteUser(int id) throws ControllerActionException {
        try {
            usersDAO.delete(id);
            userAuthorizer.removeUser(userContainer.getUser(id).getLogin());
            userContainer.removeUser(id);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! User has been not deleted.");
        }
    }

    public void editUser(User user) throws ControllerActionException {
        try {
            usersDAO.update(user);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! User has been not edited.");
        }

    }

    public String getUser(int id, String path) throws ControllerActionException {
        try {
            User user = userContainer.getUser(id);
            if (user == null)
                throw new ControllerActionException("Incorrect id! User has been not found.");
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(User.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(user, file);
            return xmlUtils.parseXmlToString(path);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsers(String path) {
        try {
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(UserContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(userContainer, file);
            return xmlUtils.parseXmlToString(path);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedUsers(String column, String criteria, String path) throws ControllerActionException {
        try {
            List<User> sortedUsers = usersDAO.getSortedByCriteria(column, criteria);
            UserContainer sortedUserContainer = new UserContainer();
            if (sortedUsers != null)
                for (User user : sortedUsers)
                    sortedUserContainer.addUser(user);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(UserContainer.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedUserContainer, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public void editUserRole(int id, String role) throws ControllerActionException {
        String oldRole = userContainer.getUser(id).getRole();
        try {
            userContainer.setRole(id, role);
            usersDAO.update(userContainer.getUser(id));
        } catch (SQLException e) {
            userContainer.setRole(id, oldRole);
            throw new ControllerActionException("Error! User's role has been not edited.");
        }
    }

    public User getUserObject(int id) {
        return userContainer.getUser(id);
    }


    public void addTask(String name, String description, Date notificationDate, Date plannedDate, Integer journalId) throws ControllerActionException {
        try {
            Task task = tasksDAO.create(name, TaskStatus.Planned, description, notificationDate, plannedDate, journalId);
            journalContainer.getJournal(journalId).addTask(task);
            notifier.addNotification(task);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Task has not been added.");
        }
    }

    public void deleteTask(Task task) throws ControllerActionException {
        try {
            tasksDAO.delete(task.getId());
            journalContainer.getJournal(task.getJournalId()).removeTask(task.getId());
            notifier.cancelNotification(task.getId());
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Task has not been deleted.");
        }
    }

    public void editTask(Task task) throws ControllerActionException {
        try {
            if (task.isRescheduled() && statusManager.isStatusConversionValid(task.getStatus(), TaskStatus.Rescheduled)) {
                task.setStatus(TaskStatus.Rescheduled);
                notifier.editNotification(task);
            }
            if (task.getStatus() == TaskStatus.Completed || task.getStatus() == TaskStatus.Cancelled)
                notifier.cancelNotification(task.getId());
            tasksDAO.update(task);

        } catch (SQLException e) {
            throw new ControllerActionException("Error! Task has not been edited.");
        }
    }

    public String getTask(int journalId, int taskId, String path) {
        try {
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(Task.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer.getJournal(journalId).getTask(taskId), file);
            return xmlUtils.parseXmlToString(path);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTasks(int journalId, String path) {
        try {
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer.getJournal(journalId), file);
            return xmlUtils.parseXmlToString(path);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedTasks(String column, String criteria, String path) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getSortedByCriteria(column, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(Journal.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedTasksJournal, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredTasksByPattern(String column, String pattern, String criteria, String path) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getFilteredByPattern(column, pattern, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(Journal.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedTasksJournal, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredTasksByEquals(String column, String equal, String criteria, String path) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getFilteredByEquals(column, equal, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(Journal.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedTasksJournal, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public Task getTaskObject(int journalId, int taskId) {
        return journalContainer.getJournal(journalId).getTask(taskId);
    }

    public void setOverdue(Task task) {
        try {
            if (statusManager.isStatusConversionValid(task.getStatus(), TaskStatus.Overdue)) {
                task.setStatus(TaskStatus.Overdue);
                editTask(task);
            }
        } catch (ControllerActionException e) {
            e.printStackTrace();
        }
    }


    public void addJournal(String name, String description, Integer userId) throws ControllerActionException {
        try {
            Journal journal = journalDAO.create(name, description, userId);
            journalContainer.addJournal(journal);
            journalNamesContainer.addName(journal.getName());
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Journal has not been added.");
        }
    }

    public void deleteJournal(int id) throws ControllerActionException {
        try {
            journalDAO.delete(id);
            journalNamesContainer.deleteName(journalContainer.getJournal(id).getName());
            journalContainer.removeJournal(id);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Journal has not been deleted.");
        }
    }

    //перед вызовом метода запомнить старое имя, необходимо для удаления, только потом менять данные и отправлять объектом
    public void editJournal(Journal journal, String oldName) throws ControllerActionException {
        try {
            journalDAO.update(journal);
            journalNamesContainer.editName(oldName, journal.getName());
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Journal has not been edited.");
        }
    }

    public String getJournal(int id, String path) throws ControllerActionException {
        try {
            File file = new File(path);
            Journal journal = journalContainer.getJournal(id);
            if (journal == null)
                throw new ControllerActionException("Error! Journal has not been found.");
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journal, file);
            return xmlUtils.parseXmlToString(path);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJournals(String path) {
        try {
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(journalContainer, file);
            return xmlUtils.parseXmlToString(path);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedJournals(String column, String criteria, String path) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getSortedByCriteria(column, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedJournalContainer, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredJournalsByPattern(String column, String pattern, String criteria, String path) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getFilteredByPattern(column, pattern, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedJournalContainer, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredJournalsByEquals(String column, String equal, String criteria, String path) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getFilteredByPattern(column, equal, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            try {
                File file = new File(path);
                JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(sortedJournalContainer, file);
                return xmlUtils.parseXmlToString(path);
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public Journal getJournalObject(int id) {
        return journalContainer.getJournal(id);
    }

    public JournalContainer getJournalContainer() {
        return journalContainer;
    }

    public JournalNamesContainer getJournalNamesContainer() {
        return journalNamesContainer;
    }

    private void createUserContainer() throws ControllerActionException {
        try {
            userContainer = new UserContainer();
            for (User user : usersDAO.getAll())
                userContainer.addUser(user);
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
    }

    private void createJournalContainer() throws ControllerActionException {
        try {
            journalContainer = new JournalContainer();
            for (Journal journal : journalDAO.getAll())
                journalContainer.addJournal(journal);
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
    }
}
