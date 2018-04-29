package server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.*;

/**
 * Object with information user
 */

@Entity
@Table(name = "\"Users\"", schema = "public", catalog = "cracker")

@XmlType(propOrder = {"id", "login", "password", "role", "registrationDate"}, name = "user")
@XmlRootElement(name = "user")
@XmlSeeAlso(Journal.class)
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "\"auto_increment\"", allocationSize = 1)
    @Column(name = "\"User_id\"", nullable = false, unique = true)
    @XmlElement(name = "id")
    private Integer id;

    @Column(name = "\"Login\"", nullable = false, length = 18, unique = true)
    @XmlElement(name = "login")
    private String login;

    @Column(name = "\"Password\"", nullable = false, length = 40)
    @XmlElement(name = "password")
    private String password;

    @Column(name = "\"Role\"", nullable = false, length = 18)
    @XmlElement(name = "role")
    private String role;

    @Column(name = "\"Registration_date\"", nullable = false)
    @XmlElement(name = "registrationDate")
    private Date registrationDate;

    @Transient
    @XmlElement(name = "journals")
    private Map<Integer, Journal> journals;

    public User() {
        this.journals = new LinkedHashMap<>();
    }

    public User(int id, String login, String password, String role, Date registrationDate) {
        this.journals = new LinkedHashMap<>();
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    public User(String login, String password, String role, Date registrationDate) {
        this.journals = new LinkedHashMap<>();
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

    public void addJournal(Journal journal) {
        journals.put(journal.getId(), journal);
    }

    public void removeJournal(int journalId) {
        journals.remove(journalId);
    }

    public Journal getJournal(int journalId) { return journals.get(journalId);}

    public List<Journal> getJournals() {
        List<Journal> list = new LinkedList<>(journals.values());
        return Collections.unmodifiableList(list);
    }

    public void setJournals(Map<Integer, Journal> journals) {
        this.journals = journals;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role) &&
                Objects.equals(registrationDate, user.registrationDate) &&
                Objects.equals(journals, user.journals);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, login, password, role, registrationDate, journals);
    }
}
