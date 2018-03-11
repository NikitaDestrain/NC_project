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
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class Controller {
    private JournalContainer journalContainer;
    private UserContainer userContainer;
    private JournalNamesContainer journalNamesContainer;
    private TaskNamesContainer taskNamesContainer;
    private LifecycleManager statusManager;
    private PostgreSQLDAOFactory postgreSQLDAOFactory;
    private PostgreSQLUsersDAO usersDAO;
    private PostgreSQLJournalDAO journalDAO;
    private PostgreSQLTasksDAO tasksDAO;
    private UserAuthorizer userAuthorizer;
    private Notifier notifier;
    private static Controller instance;

    private Controller() {
        journalNamesContainer = new JournalNamesContainer();
        taskNamesContainer = new TaskNamesContainer();
        postgreSQLDAOFactory = PostgreSQLDAOFactory.getInstance();
        statusManager = LifecycleManager.getInstance();
        usersDAO = (PostgreSQLUsersDAO) postgreSQLDAOFactory.getUsersDao();
        journalDAO = (PostgreSQLJournalDAO) postgreSQLDAOFactory.getJournalDao();
        tasksDAO = (PostgreSQLTasksDAO) postgreSQLDAOFactory.getTasksDao();
        userAuthorizer = UserAuthorizer.getInstance();
        notifier = new Notifier();
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
                throw new ControllerActionException("Error! User has been not added. Try later.");
            }
        } else
            throw new ControllerActionException("Error! Login already exists.");
    }

    public void deleteUser(int id) throws ControllerActionException {
        try {
            usersDAO.delete(id);
            userAuthorizer.removeUser(userContainer.getUser(id).getLogin());
            userContainer.removeUser(id);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! User has been not deleted. Try later.");
        }
    }

    public void editUser(User user) throws ControllerActionException {
        try {
            usersDAO.update(user);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! User has been not edited. Try later.");
        }

    }

    public String getUser(int id) throws ControllerActionException {
        try {
            User user = userContainer.getUser(id);
            if (user == null)
                throw new ControllerActionException("Incorrect id! User has been not found.");
            return marshalToString(User.class, user);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsers() {
        try {
            return marshalToString(UserContainer.class, userContainer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedUsers(String column, String criteria) throws ControllerActionException {
        try {
            List<User> sortedUsers = usersDAO.getSortedByCriteria(column, criteria);
            UserContainer sortedUserContainer = new UserContainer();
            if (sortedUsers != null)
                for (User user : sortedUsers)
                    sortedUserContainer.addUser(user);
            try {
                return marshalToString(UserContainer.class, sortedUserContainer);
            } catch (JAXBException e) {
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
            throw new ControllerActionException("Error! User's role has been not edited. Try later.");
        }
    }

    public User getUserObject(int id) {
        return userContainer.getUser(id);
    }


    public void addTask(String name, String description, Date notificationDate, Date plannedDate, Integer journalId) throws ControllerActionException {
        try {
            if (taskNamesContainer.isContain(name))
                throw new ControllerActionException("Error! Name already exists.");
            Task task = tasksDAO.create(name, TaskStatus.Planned, description, notificationDate, plannedDate, journalId);
            journalContainer.getJournal(journalId).addTask(task);
            notifier.addNotification(task);
            taskNamesContainer.addName(name);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Task has not been added. Try later.");
        }
    }

    public void deleteTask(Task task) throws ControllerActionException {
        try {
            tasksDAO.delete(task.getId());
            journalContainer.getJournal(task.getJournalId()).removeTask(task.getId());
            notifier.cancelNotification(task.getId());
            taskNamesContainer.deleteName(task.getName());
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Task has not been deleted. Try later.");
        }
    }

    public void editTask(int taskId, int oldJournalId, String name, TaskStatus status, String description, Date notificationDate,
                         Date plannedDate, String newJournalName) throws ControllerActionException {
        if (name.equals(""))
            throw new ControllerActionException("Error! Name can not be empty.");

        if (taskNamesContainer.isContain(name))
            throw new ControllerActionException("Error! Name already exists.");

        Journal oldJournal = getJournalObject(oldJournalId);
        if (oldJournal == null)
            throw new ControllerActionException("Error! Journal has not been found.");
        Task task = oldJournal.getTask(taskId);
        Journal newJournal = getJournalObject(newJournalName);
        int newJournalId = -1;
        if (newJournal != null)
            newJournalId = newJournal.getId();

        //backup
        String oldName = task.getName();
        TaskStatus oldStatus = task.getStatus();
        String oldDescription = task.getDescription();
        Date oldNotificationDate = task.getNotificationDate();
        Date oldPlannedDate = task.getPlannedDate();
        Date oldChangeDate = task.getChangeDate();

        if (status != null)
            setAllDataInTask(task, name, status, description, notificationDate, plannedDate,
                    new Date(System.currentTimeMillis()), newJournalId);
        else
            setAllDataInTask(task, name, oldStatus, description, notificationDate, plannedDate,
                    new Date(System.currentTimeMillis()), newJournalId);
        if (newJournalId != -1)
            replaceTask(taskId, oldJournalId, newJournalId);

        try {
            if (task.isRescheduled() && statusManager.isStatusConversionValid(task.getStatus(), TaskStatus.Rescheduled)) {
                task.setStatus(TaskStatus.Rescheduled);
                notifier.editNotification(task);
            }
            if (task.getStatus() == TaskStatus.Completed || task.getStatus() == TaskStatus.Cancelled)
                notifier.cancelNotification(task.getId());
            tasksDAO.update(task);
        } catch (SQLException e) {
            setAllDataInTask(task, oldName, oldStatus, oldDescription, oldNotificationDate, oldPlannedDate, oldChangeDate, oldJournalId);
            if (newJournalId != -1)
                replaceTask(taskId, newJournalId, oldJournalId);
            throw new ControllerActionException("Error! Task has not been edited. Try later.");
        }
    }

    private void setAllDataInTask(Task task, String name, TaskStatus status, String description, Date notificationDate,
                                  Date plannedDate, Date changeDate, int newJournalId) {
        task.setName(name);
        task.setStatus(status);
        task.setDescription(description);
        task.setNotificationDate(notificationDate);
        task.setPlannedDate(plannedDate);
        if (newJournalId != -1)
            task.setJournalId(newJournalId);
        task.setChangeDate(changeDate);
    }

    private void replaceTask(int taskId, int oldJournalId, int newJournalId) {
        Journal oldJournal = getJournalObject(oldJournalId);
        Task task = oldJournal.getTask(taskId);
        Journal newJournal = getJournalObject(newJournalId);

        oldJournal.removeTask(taskId);
        newJournal.addTask(task);
    }

    public String getTask(int journalId, int taskId) {
        try {
            return marshalToString(Task.class, journalContainer.getJournal(journalId).getTask(taskId));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTasks(int journalId) {
        try {
            return marshalToString(Journal.class, journalContainer.getJournal(journalId));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedTasks(String column, String criteria) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getSortedByCriteria(column, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            try {
                return marshalToString(Journal.class, sortedTasksJournal);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredTasksByPattern(String column, String pattern, String criteria) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getFilteredByPattern(column, pattern, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            try {
                return marshalToString(Journal.class, sortedTasksJournal);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredTasksByEquals(String column, String equal, String criteria) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getFilteredByEquals(column, equal, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            try {
                return marshalToString(Journal.class, sortedTasksJournal);
            } catch (JAXBException e) {
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
                editTask(task.getId(), task.getJournalId(), task.getName(), task.getStatus(), task.getDescription(),
                        task.getNotificationDate(), task.getPlannedDate(), getJournalObject(task.getJournalId()).getName());
            }
        } catch (ControllerActionException e) {
            e.printStackTrace();
        }
    }


    public void addJournal(String name, String description, Integer userId) throws ControllerActionException {
        if (journalNamesContainer.isContain(name))
            throw new ControllerActionException("Error! Name already exists.");
        try {
            Journal journal = journalDAO.create(name, description, userId);
            journalContainer.addJournal(journal);
            journalNamesContainer.addName(journal.getName());
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Journal has not been added. Try later.");
        }
    }

    public void deleteJournal(int id) throws ControllerActionException {
        try {
            journalDAO.delete(id);
            journalNamesContainer.deleteName(journalContainer.getJournal(id).getName());
            journalContainer.removeJournal(id);
        } catch (SQLException e) {
            throw new ControllerActionException("Error! Journal has not been deleted. Try later.");
        }
    }

    public void editJournal(int journalId, String name, String description) throws ControllerActionException {
        if (name.equals(""))
            throw new ControllerActionException("Error! Name can not be empty.");

        if (journalNamesContainer.isContain(name))
            throw new ControllerActionException("Error! Name already exists.");

        Journal journal = journalContainer.getJournal(journalId);
        if (journal == null)
            throw new ControllerActionException("Error! Journal has not been found.");

        //backup
        String oldName = journal.getName();
        String oldDescription = journal.getDescription();

        setAllDataInJournal(journal, name, description);

        try {
            journalDAO.update(journal);
            journalNamesContainer.editName(oldName, name);
        } catch (SQLException e) {
            setAllDataInJournal(journal, oldName, oldDescription);
            throw new ControllerActionException("Error! Journal has not been edited. Try later.");
        }
    }

    private void setAllDataInJournal(Journal journal, String name, String description) {
        journal.setName(name);
        journal.setDescription(description);
    }

    public String getJournal(int id) throws ControllerActionException {
        try {
            Journal journal = journalContainer.getJournal(id);
            if (journal == null)
                throw new ControllerActionException("Error! Journal has not been found. Try later.");
            return marshalToString(Journal.class, journal);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJournals() {
        try {
            return marshalToString(JournalContainer.class, journalContainer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSortedJournals(String column, String criteria) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getSortedByCriteria(column, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            try {
                return marshalToString(JournalContainer.class, sortedJournalContainer);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredJournalsByPattern(String column, String pattern, String criteria) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getFilteredByPattern(column, pattern, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            try {
                return marshalToString(JournalContainer.class, sortedJournalContainer);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
        return null;
    }

    public String getFilteredJournalsByEquals(String column, String equal, String criteria) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getFilteredByPattern(column, equal, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            try {
                return marshalToString(JournalContainer.class, sortedJournalContainer);
            } catch (JAXBException e) {
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

    public Journal getJournalObject(String name) {
        if (!name.equals(""))
            return journalContainer.getJournal(name);
        return null;
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
            for (Journal journal : journalDAO.getAll()) {
                journalContainer.addJournal(journal);
                journalNamesContainer.addName(journal.getName());
                for (Task task : tasksDAO.getAll())
                    if (task.getJournalId() == journal.getId()) {
                        journal.addTask(task);
                        taskNamesContainer.addName(task.getName());
                    }
            }
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
    }

    private String marshalToString(Class className, Object object) throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(className);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, sw);
        return sw.toString();
    }
}
