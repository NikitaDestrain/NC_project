package server.controller;

import auxiliaryclasses.ConstantsClass;
import database.hibernate.HibernateDAOManager;
import database.hibernate.HibernateJournalDAO;
import database.hibernate.HibernateTasksDAO;
import database.hibernate.HibernateUsersDAO;
import database.postgresql.PostgreSQLDAOManager;
import server.exceptions.ControllerActionException;
import server.exceptions.DAOFactoryActionException;
import server.exceptions.UserAuthorizerStartException;
import server.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    private JournalContainer journalContainer;
    private UserContainer userContainer;
    private JournalNamesContainer journalNamesContainer;
    private TaskNamesContainer taskNamesContainer;
    private LifecycleManager statusManager;
    private HibernateDAOManager DAOFactory;
    private HibernateUsersDAO usersDAO;
    private HibernateJournalDAO journalDAO;
    private HibernateTasksDAO tasksDAO;
    private UserAuthorizer userAuthorizer;
    private Notifier notifier;
    private static Controller instance;
    private List<Integer> systemIds;

    private Controller() throws ControllerActionException {
        try {
            systemIds = new LinkedList<>();
            journalNamesContainer = new JournalNamesContainer();
            taskNamesContainer = new TaskNamesContainer();
            DAOFactory = HibernateDAOManager.getInstance();
            statusManager = LifecycleManager.getInstance();
            usersDAO = (HibernateUsersDAO) DAOFactory.getUsersDao();
            journalDAO = (HibernateJournalDAO) DAOFactory.getJournalDao();
            tasksDAO = (HibernateTasksDAO) DAOFactory.getTasksDao();
            userAuthorizer = UserAuthorizer.getInstance();
            notifier = new Notifier();
            createUserContainer();
            createJournalContainer();
        } catch (UserAuthorizerStartException e) {
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
                User user = usersDAO.create(login, password, role);
                userContainer.addUser(user);
                userAuthorizer.addUser(user);
                systemIds.add(user.getId());
            } catch (SQLException e) {
                throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_USER);
            }
        } else
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LOGIN_EXISTS);
    }

    public void deleteUser(int id) throws ControllerActionException {
        try {
            usersDAO.delete(userContainer.getUser(id));
            userAuthorizer.removeUser(userContainer.getUser(id).getLogin());
            userContainer.removeUser(id);
            systemIds.remove(new Integer(id));
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_DELETE_USER);
        }
    }

    public void editUser(User user) throws ControllerActionException {
        try {
            usersDAO.update(user);
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
            throw new ControllerActionException(ConstantsClass.ERROR_LAZY_MESSAGE);
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
                Task task = tasksDAO.create(name, TaskStatus.Planned, description, notificationDate, plannedDate, journalId);
                journalContainer.getJournal(journalId).addTask(task);
                notifier.addNotification(task);
                taskNamesContainer.addName(name);
                systemIds.add(task.getId());
            }
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_TASK);
        }
    }

    //метод добавления, если айди уже существует и не требуется сиквенс
    public void addTask(Task t) throws ControllerActionException {
        try {
            if (taskNamesContainer.isContain(t.getName()))
                throw new ControllerActionException(ControllerErrorConstants.ERROR_NAME_EXISTS);

            if (checkDate(t.getPlannedDate(), t.getNotificationDate())) {
                Task task = tasksDAO.create(t.getId(), t.getName(), TaskStatus.Planned, t.getDescription(), t.getNotificationDate(),
                        t.getPlannedDate(), t.getJournalId());
                journalContainer.getJournal(t.getJournalId()).addTask(task);
                notifier.addNotification(task);
                taskNamesContainer.addName(t.getName());
                systemIds.add(task.getId());
            }
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_TASK);
        }
    }

    public void deleteTask(Task task) throws ControllerActionException {
        try {
            tasksDAO.delete(task);
            journalContainer.getJournal(task.getJournalId()).removeTask(task.getId());
            notifier.cancelNotification(task.getId());
            taskNamesContainer.deleteName(task.getName());
            systemIds.remove(new Integer(task.getId()));
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

        int newJournalId = -1;
        if (newJournalName != null) {
            Journal newJournal = getJournal(newJournalName);
            if (newJournal != null)
                newJournalId = newJournal.getId();
        }

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
            taskNamesContainer.editName(oldName, name);
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

    public Task getTask(int taskId) {
        Task task;
        for (Journal journal : journalContainer.getJournals()) {
            task = journal.getTask(taskId);
            if (task != null)
                return task;
        }
        return null;
    }

    public Journal getTasks(int journalId) {
        return journalContainer.getJournal(journalId);
    }

    public Journal getSortedTasks(int journalId, String column, String criteria) throws ControllerActionException {
        try {
            return getSortedTasksJournal(tasksDAO.getSortedByCriteria(journalId, column, criteria));
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    public Journal getFilteredTasksByPattern(int journalId, String column, String pattern, String criteria) throws ControllerActionException {
        try {
            return getSortedTasksJournal(tasksDAO.getFilteredByPattern(journalId, column, pattern, criteria));
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    public Journal getFilteredTasksByEquals(int journalId, String column, String equal, String criteria) throws ControllerActionException {
        try {
            return getSortedTasksJournal(tasksDAO.getFilteredByEquals(journalId, column, equal, criteria));
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    private Journal getSortedTasksJournal(List<Task> sortedTasks) {
        fixTasks(sortedTasks);
        Journal sortedTasksJournal = new Journal();
        if (sortedTasks != null)
            for (Task task : sortedTasks)
                sortedTasksJournal.addTask(task);
        return sortedTasksJournal;
    }

    public void renameTasks(int journalId, List<Integer> tasksId, String prefix) throws ControllerActionException {
        for (Task task : journalContainer.getJournal(journalId).getTasks())
            if (tasksId.contains(task.getId()) && checkLengthName(task.getName(), prefix))
                doRenameAction(task, prefix);
    }

    private boolean checkLengthName(String name, String prefix) {
        return (name.length() + prefix.length()) < 50;
    }

    private void doRenameAction(Task task, String prefix) throws ControllerActionException {
        try {
            String oldName = task.getName();
            String newName = createNewName(prefix, oldName);
            task.setName(newName);
            tasksDAO.update(task);
            taskNamesContainer.deleteName(oldName);
            taskNamesContainer.addName(newName);
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_LAZY_MESSAGE);
        }
    }

    private String createNewName(String prefix, String name) {
        return prefix + "." + name;
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
            Journal journal = journalDAO.create(name, description, userId);
            journalContainer.addJournal(journal);
            journalNamesContainer.addName(journal.getName());
            systemIds.add(journal.getId());
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_JOURNAL);
        }
    }

    public Journal addJournal(Journal j) throws ControllerActionException {
        if (journalNamesContainer.isContain(j.getName()))
            throw new ControllerActionException(ControllerErrorConstants.ERROR_NAME_EXISTS);
        try {
            Journal journal = journalDAO.create(j.getId(), j.getName(), j.getDescription(), j.getUserId());
            journalContainer.addJournal(journal);
            journalNamesContainer.addName(journal.getName());
            systemIds.add(journal.getId());
            return journal;
        } catch (SQLException e) {
            throw new ControllerActionException(ControllerErrorConstants.ERROR_ADD_JOURNAL);
        }
    }

    public void deleteJournal(int id) throws ControllerActionException {
        try {
            journalDAO.delete(journalContainer.getJournal(id));
            journalNamesContainer.deleteName(journalContainer.getJournal(id).getName());
            journalContainer.removeJournal(id);
            systemIds.remove(new Integer(id));
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
            journalDAO.update(journal);
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
            return getSortedJournalContainer(journalDAO.getSortedByCriteria(column, criteria));
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    public JournalContainer getFilteredJournalsByPattern(String column, String pattern, String criteria) throws ControllerActionException {
        try {
            return getSortedJournalContainer(journalDAO.getFilteredByPattern(column, pattern, criteria));
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    public JournalContainer getFilteredJournalsByEquals(String column, String equal, String criteria) throws ControllerActionException {
        try {
            return getSortedJournalContainer(journalDAO.getFilteredByEquals(column, equal, criteria));
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    private JournalContainer getSortedJournalContainer(List<Journal> sortedJournals) {
        JournalContainer sortedJournalContainer = new JournalContainer();
        if (sortedJournals != null)
            for (Journal journal : sortedJournals)
                sortedJournalContainer.addJournal(journal);
        return sortedJournalContainer;
    }

    public JournalNamesContainer getJournalNamesContainer() {
        return journalNamesContainer;
    }

    private void createUserContainer() throws ControllerActionException {
        try {
            userContainer = new UserContainer();
            for (User user : usersDAO.getAll()) {
                userContainer.addUser(user);
                systemIds.add(user.getId());
            }
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    private void createJournalContainer() throws ControllerActionException {
        try {
            journalContainer = new JournalContainer();
            for (Journal journal : journalDAO.getAll()) {
                systemIds.add(journal.getId());
                journalContainer.addJournal(journal);
                journalNamesContainer.addName(journal.getName());
                List<Task> tasks = tasksDAO.getAll();
                fixTasks(tasks);
                for (Task task : tasks)
                    if (task.getJournalId() == journal.getId()) {
                        notifier.addNotification(task);
                        journal.addTask(task);
                        taskNamesContainer.addName(task.getName());
                        systemIds.add(task.getId());
                    }
            }
        } catch (SQLException e) {
            throw new ControllerActionException(e.getMessage());
        }
    }

    private void fixTasks(List<Task> tasks) {
        for (Task task : tasks) {
            task.setStringStatus(task.getStringStatus());
            task.setPlannedDate(task.getPlannedDate());
            task.setUploadDate(task.getUploadDate());
            task.setNotificationDate(task.getNotificationDate());
            task.setChangeDate(task.getChangeDate());
        }
    }

    public List<Journal> createJournalListByIds(List<Integer> journalIDs) {
        List<Journal> list = new LinkedList<>();
        for (Integer id : journalIDs) {
            list.add(getJournal(id));
        }
        return Collections.unmodifiableList(list);
    }

    public List<Task> createTaskListByIds(List<Integer> taskIDs) {
        List<Task> list = new LinkedList<>();
        for (Integer id : taskIDs) {
            list.add(getTask(id));
        }
        return Collections.unmodifiableList(list);
    }

    public boolean containsId(int id) {
        return systemIds.contains(id);
    }

    public boolean containsObject(Object object) {
        if (object instanceof Journal) {
            return journalDAO.contains(((Journal) object).getId()) || journalDAO.contains(((Journal) object).getName());
        } else if (object instanceof Task) {
            return tasksDAO.contains(((Task) object).getId()) || tasksDAO.contains(((Task) object).getName());
        } else return false;
    }

    public boolean isExistId(int id) {
        return tasksDAO.contains(id) || usersDAO.contains(id) || journalDAO.contains(id);
    }
}
