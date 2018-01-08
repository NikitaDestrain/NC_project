package server.controller;

import auxiliaryclasses.ConstantsClass;
import server.commandproccessor.User;
import server.properties.ParserProperties;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserAuthorizer {
    private Map<String, String> userData;
    private UserDataSerializer userDataSerializer;
    private static UserAuthorizer instance;

    private UserAuthorizer() {
        this.userDataSerializer = new UserDataSerializer();
        try {
            this.userData = userDataSerializer.readData(ParserProperties.getInstance()
                    .getProperty(ConstantsClass.USER_DATA));
        } catch (IOException e) {
            if (JOptionPane.showConfirmDialog(null,
                    "Could not load user data from file!\nDo you want to create new file with user's data?\n" +
                            "If you choose NO, the program execution will be stopped!",
                    "Error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                userData = new HashMap<>();
            }
            else System.exit(1);
        }
    }

    public static synchronized UserAuthorizer getInstance() { //todo vlla synchronized DONE
        if (instance == null) instance = new UserAuthorizer();
        return instance;
    }

    /**
     * Checks if user with current login exists in user's map and its password equals password from parameter
     */

    public boolean isUserDataCorrect(User user) {
        if (user == null) return false;
        return userData.containsKey(user.getLogin()) &&
                userData.get(user.getLogin()).equals(user.getPassword());
    }

    public boolean isSuchLoginExists(String login) {
        return userData.containsKey(login);
    }

    public void addUser(User user) {
        if (user != null) {
            userData.put(user.getLogin(), user.getPassword());
        }
    }

    public void writeUserData(String path) {
        try {
            userDataSerializer.writeData(this.userData, path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not write user data to file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
