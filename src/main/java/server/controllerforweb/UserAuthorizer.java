package server.controllerforweb;

import database.postgresql.PostgreSQLDAOFactory;
import server.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAuthorizer {
    private Map<String, String> userData;

    private static UserAuthorizer instance;

    private UserAuthorizer() {
        userData = new HashMap<>();
        try {
            createStartUserData();
        } catch (SQLException e) {
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
        return userData.containsKey(login) &&
                userData.get(login).equals(password);
    }

    public boolean isSuchLoginExists(String login) {
        return userData.containsKey(login);
    }

    public void addUser(User user) {
        addUser(user.getLogin(), user.getPassword());
    }

    private void addUser(String login, String password) {
        if (login != null && password != null)
            userData.put(login, password);
    }

    private void createStartUserData() throws SQLException {
        List<User> users = PostgreSQLDAOFactory.getInstance().getUsersDao().getAll();
        for (User user: users)
            addUser(user.getLogin(), user.getPassword());
    }
}
