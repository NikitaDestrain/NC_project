package server.controllerforweb;

import database.daointerfaces.UsersDAO;
import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLUsersDAO;
import server.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAuthorizer {
    private Map<String, String> userData;
    private UsersDAO usersDAO = PostgreSQLDAOFactory.getInstance().getUsersDao();
    private User signedUser;

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

    public User getSignedUser() {
        return signedUser;
    }

    /**
     * Checks if user with current login exists in user's map and its password equals password from parameter
     */

    public boolean isUserDataCorrect(String login, String password) {
        if (login == null || password == null) return false;
        if (userData.containsKey(login) &&
                userData.get(login).equals(password)) {
            try {
                signedUser = usersDAO.getByLoginAndPassword(login, password);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isSuchLoginExists(String login) {
        return userData.containsKey(login);
    }

    public void addUser(String login, String password) {
        addUserByLoginAndPassword(login, password);
        try {
            signedUser = usersDAO.getByLoginAndPassword(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        addUserByLoginAndPassword(user.getLogin(), user.getPassword());
    }

    private void addUserByLoginAndPassword(String login, String password) {
        if (login != null && password != null) {
            userData.put(login, password);
            try {
                usersDAO.create(login, password, "user");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeUser(String login) {
        userData.remove(login);
    }

    private void createStartUserData() throws SQLException {
        List<User> users = usersDAO.getAll();
        for (User user: users)
            addUser(user.getLogin(), user.getPassword());
    }
}
