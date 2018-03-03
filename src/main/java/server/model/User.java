package server.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Object with information user
 */

@XmlType(propOrder = {"id", "login", "password", "role", "registrationDate"}, name = "user")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
    @XmlElement(name = "id")
    private Integer id;
    @XmlElement(name = "login")
    private String login;
    @XmlElement(name = "password")
    private String password;
    @XmlElement(name = "role")
    private String role;
    @XmlElement(name = "registrationDate")
    private Date registrationDate;

    public User() {
    }

    public User(int id, String login, String password, String role, Date registrationDate) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.registrationDate = registrationDate;
        this.registrationDate = new Date(System.currentTimeMillis());
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +

                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", registrationDate='" + registrationDate +
                '}';
    }
}
