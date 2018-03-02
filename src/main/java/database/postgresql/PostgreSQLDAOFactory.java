package database.postgresql;

import database.daointerfaces.DAOFactory;
import database.daointerfaces.JournalDAO;
import database.daointerfaces.TasksDAO;
import database.daointerfaces.UsersDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostgreSQLDAOFactory implements DAOFactory {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "root";
    private static final String DRIVER_NAME = "org.postgresql.Driver";
    private static final String START_SCRIPT_NAME = "scripts/databasescript.sql";
    private static PostgreSQLDAOFactory instance;
    private Connection connection;

    private PostgreSQLDAOFactory() {
        setConnection();
        executeSqlStartScript();
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

    public void executeSqlStartScript() {
        String delimiter = ";";
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(START_SCRIPT_NAME)).useDelimiter(delimiter);
            Statement currentStatement = null;
            while (scanner.hasNext()) {
                String rawStatement = scanner.next() + delimiter;
                try {
                    currentStatement = connection.createStatement();
                    currentStatement.execute(rawStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (currentStatement != null) {
                        try {
                            currentStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    currentStatement = null;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}