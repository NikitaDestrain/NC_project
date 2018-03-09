package database.postgresql;

import database.daointerfaces.DAOFactory;
import database.daointerfaces.JournalDAO;
import database.daointerfaces.TasksDAO;
import database.daointerfaces.UsersDAO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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
    private DataSource dataSource;
    private Connection connection;

    private PostgreSQLDAOFactory(String path) {
        connection = getConnection();
        executeSqlStartScript(path);
    }

    private PostgreSQLDAOFactory() {
        connection = getConnection();
    }

    public static PostgreSQLDAOFactory getInstance(String path) {
        if (instance == null)
            instance = new PostgreSQLDAOFactory(path);
        return instance;
    }

    public static PostgreSQLDAOFactory getInstance() {
        if (instance == null)
            instance = new PostgreSQLDAOFactory();
        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            dataSource = (DataSource) env.lookup("jdbc/cracker");
            return dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    private void executeSqlStartScript(String path) {
        String delimiter = ";";
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(path)).useDelimiter(delimiter);
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