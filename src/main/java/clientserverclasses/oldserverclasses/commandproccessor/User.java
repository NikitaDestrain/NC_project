package clientserverclasses.oldserverclasses.commandproccessor;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Object with login, password and port of a user
 */

@XmlType(propOrder = {"login", "password", "port"}, name = "user")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
    @XmlElement(name = "login")
    private String login;
    @XmlElement(name = "password")
    private String password;
    @XmlElement(name = "port")
    private Integer port;

    public User() {
    }

    public User(String login, String password, Integer port) {
        this.login = login;
        this.password = password;
        this.port = port;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", port='" + port +
                '}';
    }
}
