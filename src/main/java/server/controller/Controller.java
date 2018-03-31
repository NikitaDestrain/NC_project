package server.controller;

import database.hibernate.HibernateDAOFactory;
import database.hibernate.HibernateJournalDAO;
import database.hibernate.HibernateTasksDAO;
import database.hibernate.HibernateUsersDAO;
import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLJournalDAO;
import database.postgresql.PostgreSQLTasksDAO;
import database.postgresql.PostgreSQLUsersDAO;
import server.exceptions.ControllerActionException;
import server.exceptions.DAOFactoryActionException;
import server.exceptions.UserAuthorizerStartException;
import server.model.*;

import java.sql.Date;
import java.sql.SQLException;
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
    private HibernateTasksDAO hibernateTasksDAO;
    private HibernateJournalDAO hibernateJournalDAO;
    private HibernateUsersDAO hibernateUsersDAO;
    private UserAuthorizer userAuthorizer;
    private Notifier notifier;
    private static Controller instance;

    private Controller() throws ControllerActionException {
        try {
            journalNamesContainer = new JournalNamesContainer();
            taskNamesContainer = new TaskNamesContainer();
            postgreSQLDAOFactory = PostgreSQLDAOFactory.getInstance();
            statusManager = LifecycleManager.getInstance();
            usersDAO = (PostgreSQLUsersDAO) postgreSQLDAOFactory.getUsersDao();
            journalDAO = (PostgreSQLJournalDAO) postgreSQLDAOFactory.getJournalDao();
            tasksDAO = (PostgreSQLTasksDAO) postgreSQLDAOFactory.getTasksDao();
            hibernateTasksDAO = (HibernateTasksDAO) HibernateDAOFactory.getInstance().getTasksDao();
            hibernateJournalDAO = (HibernateJournalDAO) HibernateDAOFactory.getInstance().getJournalDao();
            hibernateUsersDAO = (HibernateUsersDAO) HibernateDAOFactory.getInstance().getUsersDao();
            userAuthorizer = UserAuthorizer.getInstance();
            notifier = new Notifier();
            createUserContainer();
            createJournalContainer();
        } catch (DAOFactoryActionException | UserAuthorizerStartException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    public static synchronized Controller getInstance() throws ControllerActionException {
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
                //User user = usersDAO.create(login, password, role);
                User user = hibernateUsersDAO.create(login, password, role);
                userContainer.addUser(user);
                userAuthorizer.addUser(user);
            } catch (SQLException e) {
                throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_USER);
            }
        } else
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LOGIN_EXISTS);
    }

    public void deleteUser(int id) throws ControllerActionException {
        try {
            //usersDAO.delete(userContainer.getUser(id));
            hibernateUsersDAO.delete(userContainer.getUser(id));
            userAuthorizer.removeUser(userContainer.getUser(id).getLogin());
            userContainer.removeUser(id);
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_DELETE_USER);
        }
    }

    public void editUser(User user) throws ControllerActionException {
        try {
            //usersDAO.update(user);
            hibernateUsersDAO.update(user);
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_EDIT_USER);
        }
    }

    public User getUser(int id) {
        return userContainer.getUser(id);
    }

    public UserContainer getUsers() {
        return userContainer;
    }

    public UserContainer getSortedUsers(String column, String criteria) throws ControllerActionException {
        try {
            List<User> sortedUsers = usersDAO.getSortedByCriteria(column, criteria);
            UserContainer sortedUserContainer = new UserContainer();
            if (sortedUsers != null)
                for (User user : sortedUsers)
                    sortedUserContainer.addUser(user);
            return sortedUserContainer;
        } catch (SQLException e) {
            throw new ControllerActionException();
        }
    }

    public void editUserRole(int id, String role) throws ControllerActionException {
        String oldRole = userContainer.getUser(id).getRole();
        try {
            userContainer.setRole(id, role);
            usersDAO.update(userContainer.getUser(id));
        } catch (SQLException e) {
            userContainer.setRole(id, oldRole);
            throw new ControllerActionException(ControllerErrorConstants.ERROR_EDIT_USER_ROLE);
        }
    }


    private boolean checkDate(Date plannedDate, Date notificationDate) throws ControllerActionException {
        if (plannedDate.before(new Date(System.currentTimeMillis())))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_PAST_PLANNED_DATE);

        if (notificationDate.before(new Date(System.currentTimeMillis())))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_PAST_NOTIFICATION_DATE);

        if (!notificationDate.before(plannedDate) && !notificationDate.equals(plannedDate))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_NOTIFICATION_AFTER_PLANNED);

        return true;
    }

    public void addTask(String name, String description, Date notificationDate, Date plannedDate, Integer journalId) throws ControllerActionException {
        try {
            if (taskNamesContainer.isContain(name))
                throw new ControllerActionException(ControllerErrorConstants.ERROR_NAME_EXISTS);

            if (checkDate(plannedDate, notificationDate)) {
                //Task task = tasksDAO.create(name, TaskStatus.Planned, description, notificationDate, plannedDate, journalId);
                Task task = hibernateTasksDAO.create(name, TaskStatus.Planned, description, notificationDate, plannedDate, journalId);
                journalContainer.getJournal(journalId).addTask(task);
                notifier.addNotification(task);
                taskNamesContainer.addName(name);
            }
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_TASK);
        }
    }

    public void deleteTask(Task task) throws ControllerActionException {
        try {
            //tasksDAO.delete(task);
            hibernateTasksDAO.delete(task);
            journalContainer.getJournal(task.getJournalId()).removeTask(task.getId());
            notifier.cancelNotification(task.getId());
            taskNamesContainer.deleteName(task.getName());
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_DELETE_TASK);
        }
    }

    public void editTask(int taskId, int oldJournalId, String name, TaskStatus status, String description, Date notificationDate,
                         Date plannedDate, String newJournalName) throws ControllerActionException {
        if (name.equals(""))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_EMPTY_NAME);

        if (!checkDate(plannedDate, notificationDate))
            return;

        Journal oldJournal = getJournal(oldJournalId);
        if (oldJournal == null)
            throw new ControllerActionException(ControllerErrorConstants.ERROR_JOURNAL_NOT_FOUND);

        Task task = oldJournal.getTask(taskId);
        if (taskNamesContainer.isContain(name) && !task.getName().equals(name))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_NAME_EXISTS);

        Journal newJournal = getJournal(newJournalName);
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
            //tasksDAO.update(task);
            hibernateTasksDAO.update(task);
        } catch (SQLException e) {
            setAllDataInTask(task, oldName, oldStatus, oldDescription, oldNotificationDate, oldPlannedDate, oldChangeDate, oldJournalId);
            if (newJournalId != -1)
                replaceTask(taskId, newJournalId, oldJournalId);
            throw new ControllerActionException(ControllerErrorConstants.ERROR_EDIT_TASK);
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
        Journal oldJournal = getJournal(oldJournalId);
        Task task = oldJournal.getTask(taskId);
        Journal newJournal = getJournal(newJournalId);

        oldJournal.removeTask(taskId);
        newJournal.addTask(task);
    }

    public Task getTask(int journalId, int taskId) {
        return journalContainer.getJournal(journalId).getTask(taskId);
    }

    public Journal getTasks(int journalId) {
        return journalContainer.getJournal(journalId);
    }

    public Journal getSortedTasks(int journalId, String column, String criteria) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getSortedByCriteria(journalId, column, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            return sortedTasksJournal;
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    public Journal getFilteredTasksByPattern(int journalId, String column, String pattern, String criteria) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getFilteredByPattern(journalId, column, pattern, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            return sortedTasksJournal;
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    public Journal getFilteredTasksByEquals(int journalId, String column, String equal, String criteria) throws ControllerActionException {
        try {
            List<Task> sortedTasks = tasksDAO.getFilteredByEquals(journalId, column, equal, criteria);
            Journal sortedTasksJournal = new Journal();
            if (sortedTasks != null)
                for (Task task : sortedTasks)
                    sortedTasksJournal.addTask(task);
            return sortedTasksJournal;
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    public void setOverdue(Task task) {
        try {
            if (statusManager.isStatusConversionValid(task.getStatus(), TaskStatus.Overdue)) {
                task.setStatus(TaskStatus.Overdue);
                editTask(task.getId(), task.getJournalId(), task.getName(), task.getStatus(), task.getDescription(),
                        task.getNotificationDate(), task.getPlannedDate(), getJournal(task.getJournalId()).getName());
            }
        } catch (ControllerActionException e) {
            e.printStackTrace();
        }
    }

    public void addJournal(String name, String description, Integer userId) throws ControllerActionException {
        if (journalNamesContainer.isContain(name))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_NAME_EXISTS);
        try {
            //Journal journal = journalDAO.create(name, description, userId);
            Journal journal = hibernateJournalDAO.create(name, description, userId);
            journalContainer.addJournal(journal);
            journalNamesContainer.addName(journal.getName());
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_JOURNAL);
        }
    }

    public void deleteJournal(int id) throws ControllerActionException {
        try {
            //journalDAO.delete(journalContainer.getJournal(id));
            hibernateJournalDAO.delete(journalContainer.getJournal(id));
            journalNamesContainer.deleteName(journalContainer.getJournal(id).getName());
            journalContainer.removeJournal(id);
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_DELETE_JOURNAL);
        }
    }

    public void editJournal(int journalId, String name, String description) throws ControllerActionException {
        if (name.equals(""))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_EMPTY_NAME);

        Journal journal = journalContainer.getJournal(journalId);
        if (journal == null)
            throw new ControllerActionException(ControllerErrorConstants.ERROR_JOURNAL_NOT_FOUND);

        if (journalNamesContainer.isContain(name) && !journal.getName().equals(name))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_NAME_EXISTS);

        //backup
        String oldName = journal.getName();
        String oldDescription = journal.getDescription();

        setAllDataInJournal(journal, name, description);

        try {
            //journalDAO.update(journal);
            hibernateJournalDAO.update(journal);
            journalNamesContainer.editName(oldName, name);
        } catch (SQLException e) {
            setAllDataInJournal(journal, oldName, oldDescription);
            throw new ControllerActionException(ControllerErrorConstants.ERROR_EDIT_JOURNAL);
        }
    }

    private void setAllDataInJournal(Journal journal, String name, String description) {
        journal.setName(name);
        journal.setDescription(description);
    }

    public Journal getJournal(int id) {
        return journalContainer.getJournal(id);
    }

    public Journal getJournal(String name) {
        if (!name.equals(""))
            return journalContainer.getJournal(name);
        return null;
    }

    public JournalContainer getJournals() {
        return journalContainer;
    }

    public JournalContainer getSortedJournals(String column, String criteria) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getSortedByCriteria(column, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            return sortedJournalContainer;
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    public JournalContainer getFilteredJournalsByPattern(String column, String pattern, String criteria) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getFilteredByPattern(column, pattern, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            return sortedJournalContainer;
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    public JournalContainer getFilteredJournalsByEquals(String column, String equal, String criteria) throws ControllerActionException {
        try {
            List<Journal> sortedJournals = journalDAO.getFilteredByEquals(column, equal, criteria);
            JournalContainer sortedJournalContainer = new JournalContainer();
            if (sortedJournals != null)
                for (Journal journal : sortedJournals)
                    sortedJournalContainer.addJournal(journal);
            return sortedJournalContainer;
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
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
            throw new ControllerActionException(e.getMessage());
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
                        notifier.addNotification(task);
                        journal.addTask(task);
                        taskNamesContainer.addName(task.getName());
                    }
            }
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }
}
