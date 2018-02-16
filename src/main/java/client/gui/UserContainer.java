package client.gui;

public class UserContainer {
    private static UserContainer ourInstance = new UserContainer();
    private String username;

    public static UserContainer getInstance() {
        return ourInstance;
    }

    private UserContainer() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
