package server.commandproccessor;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(propOrder = {"login", "password"}, name = "user")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)

public class User implements Serializable {
    @XmlElement(name = "login")
    private String login;
    @XmlElement(name = "password")
    private String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
