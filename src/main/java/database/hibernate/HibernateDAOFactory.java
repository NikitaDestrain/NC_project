package database.hibernate;

import database.daointerfaces.DAOFactory;
import database.daointerfaces.JournalDAO;
import database.daointerfaces.TasksDAO;
import database.daointerfaces.UsersDAO;

public class HibernateDAOFactory implements DAOFactory {

    private static HibernateDAOFactory instance;
    private static TasksDAO tasksDAO;
    private static JournalDAO journalDAO;
    private static UsersDAO usersDAO;

    public HibernateDAOFactory() {
    }

    public static synchronized HibernateDAOFactory getInstance() {
        if (instance == null) {
            instance = new HibernateDAOFactory();
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
