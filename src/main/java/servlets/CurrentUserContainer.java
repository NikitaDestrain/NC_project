package servlets;

import server.model.User;

public class CurrentUserContainer {
    private static CurrentUserContainer ourInstance = new CurrentUserContainer();
    private User user;

    public static CurrentUserContainer getInstance() {
        return ourInstance;
    }

    private CurrentUserContainer() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
