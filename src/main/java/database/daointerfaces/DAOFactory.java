package database.daointerfaces;

import server.exceptions.DAOFactoryActionException;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAOFactory {
    public Connection getConnection() throws DAOFactoryActionException;

    public TasksDAO getTasksDao();

    public JournalDAO getJournalDao();

    public UsersDAO getUsersDao();
}
