package database.postgresql;

import database.daointerfaces.DAOFactory;
import database.daointerfaces.JournalDAO;
import database.daointerfaces.TasksDAO;
import database.daointerfaces.UsersDAO;
import server.exceptions.DAOFactoryActionException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostgreSQLDAOFactory implements DAOFactory {
    private static PostgreSQLDAOFactory instance;
    private DataSource dataSource;
    private Connection connection;

    private PostgreSQLDAOFactory(String path) throws DAOFactoryActionException {
        initDataSource();
        connection = getConnection();
        executeSqlStartScript(path);
    }

    private PostgreSQLDAOFactory() throws DAOFactoryActionException {
        initDataSource();
        connection = getConnection();
    }

    public static PostgreSQLDAOFactory getInstance(String path) throws DAOFactoryActionException {
        if (instance == null)
            instance = new PostgreSQLDAOFactory(path);
        return instance;
    }

    public static PostgreSQLDAOFactory getInstance() throws DAOFactoryActionException {
        if (instance == null)
            instance = new PostgreSQLDAOFactory();
        return instance;
    }

    private void initDataSource() throws DAOFactoryActionException {
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            dataSource = (DataSource) env.lookup("jdbc/cracker");
        } catch (NamingException e) {
            throw new DAOFactoryActionException(DAOErrorConstants.INIT_ERROR);
        }
    }

    public Connection getConnection() throws DAOFactoryActionException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DAOFactoryActionException(DAOErrorConstants.CONNECTION_ERROR);
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

    private void executeSqlStartScript(String path) throws DAOFactoryActionException {
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
                    throw new DAOFactoryActionException(DAOErrorConstants.STATEMENT_ERROR);
                } finally {
                    if (currentStatement != null) {
                        try {
                            currentStatement.close();
                        } catch (SQLException e) {
                            throw new DAOFactoryActionException(DAOErrorConstants.INCORRECT_WORK_ERROR);
                        }
                    }
                    currentStatement = null;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new DAOFactoryActionException(DAOErrorConstants.START_SCRIPT_ERROR);
        }
    }
}