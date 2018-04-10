package database.hibernate;

import database.daointerfaces.DAOManager;
import database.daointerfaces.JournalDAO;
import database.daointerfaces.TasksDAO;
import database.daointerfaces.UsersDAO;

public class HibernateDAOManager implements DAOManager {

    private static HibernateDAOManager instance;
    private static TasksDAO tasksDAO;
    private static JournalDAO journalDAO;
    private static UsersDAO usersDAO;

    public HibernateDAOManager() {
    }

    public static synchronized HibernateDAOManager getInstance() {
        if (instance == null) {
            instance = new HibernateDAOManager();
        }
        return instance;
    }

    @Override
    public TasksDAO getTasksDao() {
        if (tasksDAO == null) {
            tasksDAO = new HibernateTasksDAO();
        }
        return tasksDAO;
    }

    @Override
    public JournalDAO getJournalDao() {
        if (journalDAO == null) {
            journalDAO = new HibernateJournalDAO();
        }
        return journalDAO;
    }

    @Override
    public UsersDAO getUsersDao() {
        if (usersDAO == null) {
            usersDAO = new HibernateUsersDAO();
        }
        return usersDAO;
    }
}
