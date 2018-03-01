package database;

import database.daointerfaces.DAOFactory;
import database.daointerfaces.JournalDAO;
import database.daointerfaces.TasksDAO;
import database.daointerfaces.UsersDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLDAOFactory implements DAOFactory {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "root";
    private static final String DRIVER_NAME = "org.postgresql.Driver";
    private static PostgreSQLDAOFactory instance;
    private Connection connection;

    private PostgreSQLDAOFactory() {
        setConnection();
    }

    public static PostgreSQLDAOFactory getInstance() {
        if (instance == null)
            instance = new PostgreSQLDAOFactory();
        return instance;
    }

    private void setConnection() {
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error connection");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver was not found");
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error disconnect");
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public TasksDAO getTasksDao() {
        return new PostgreSQLTasksDAO(connection);
    }

    @Override
    public JournalDAO getJournalDao() {
        return new PostgreSQLJournalDAO(connection);
    }

    @Override
    public UsersDAO getUsersDao() {
        return new PostgreSQLUsersDAO(connection);
    }
}