package server.controller;

import database.daointerfaces.UsersDAO;
import database.postgresql.PostgreSQLDAOFactory;
import server.exceptions.DAOFactoryActionException;
import server.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAuthorizer {
    private Map<String, String> userData;
    private UsersDAO usersDAO;

    private static UserAuthorizer instance;

    private UserAuthorizer() {
        try {
            usersDAO = PostgreSQLDAOFactory.getInstance().getUsersDao();
            userData = new HashMap<>();
            createStartUserData();
        } catch (SQLException | DAOFactoryActionException e) {
            e.printStackTrace();
        }
    }

    public static synchronized UserAuthorizer getInstance() {
        if (instance == null) instance = new UserAuthorizer();
        return instance;
    }

    /**
     * Checks if user with current login exists in user's map and its password equals password from parameter
     */

    public boolean isUserDataCorrect(String login, String password) {
        if (login == null || password == null) return false;
        return userData.containsKey(login) && userData.get(login).equals(password);
    }

    public boolean isSuchLoginExists(String login) {
        return userData.containsKey(login);
    }

    public void addUser(String login, String password) {
        addUserByLoginAndPassword(login, password);
    }

    public void addUser(User user) {
        addUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    private void addUserByLoginAndPassword(String login, String password) {
        if (login != null && password != null)
            userData.put(login, password);
    }

    public void removeUser(String login) {
        userData.remove(login);
    }

    private void createStartUserData() throws SQLException {
        List<User> users = usersDAO.getAll();
        for (User user : users)
            addUser(user.getLogin(), user.getPassword());
    }
}
