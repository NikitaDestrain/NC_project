package database.daointerfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAOFactory {
    public Connection getConnection() throws SQLException;

    public TasksDAO getTasksDao();

    public JournalDAO getJournalDao();

    public UsersDAO getUsersDao();
}
