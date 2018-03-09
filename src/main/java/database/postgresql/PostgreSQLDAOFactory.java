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
        //setConnection(START_SCRIPT_NAME);
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

    public void setConnection(String pathStartScript) {
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error connection");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver was not found");
        }
        //раскоментить когда будут соединены два слоя
        /*
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            this.dataSource = (DataSource) env.lookup("jdbc/cracker");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        executeSqlStartScript(pathStartScript);
    }

    @Override
    public Connection getConnection() {
        //todo также после того как свяжем исползовать
        // todo через сервлет все норм
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            this.dataSource = (DataSource) env.lookup("jdbc/cracker");
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

    public void executeSqlStartScript(String path) {
        String delimiter = ";";
        Scanner scanner;
        try {
            //todo замменить на path
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