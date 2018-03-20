package server.factories;

import server.model.User;

import java.sql.Date;

public class UserFactory {

    public static User createUser(int id, String login, String password, String role, Date registrationDate) {
        return new User(id, login, password, role, registrationDate);
    }

    public static User createUser(User user) {
        return new User(user.getId(), user.getLogin(), user.getPassword(), user.getRole(), user.getRegistrationDate());
    }
}
