package oldserverclasses.controller;

import java.util.Map;

public class UserAuthorizer {
    private Map<String, String> userData;
    private static UserAuthorizer instance;

    private UserAuthorizer() {
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

    public void addUser(String login, String password) {
        if (login != null && password != null) {
            userData.put(login, password);
        }
    }

    public void writeUserData(String path) {
//        try {
//            userDataSerializer.writeData(this.userData, path);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Could not write user data to file!",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }
}
