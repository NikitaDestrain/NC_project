package client.commandprocessor.temporary;

import client.commandprocessor.Command;
import client.model.Journal;
import client.model.Task;
import server.controller.Serializer;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(propOrder = {"name", "login", "password"}, name = "authCommand")
@XmlRootElement(name = "authCommand")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlSeeAlso({Task.class, Journal.class})
public class AuthCommand implements Serializable {
    @XmlElement(name = "name")
    private String name;
    @XmlElement (name = "login")
    private String login;
    @XmlElement (name = "password")
    private String password;

    public AuthCommand() {}

    public AuthCommand(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "AuthCommand{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
