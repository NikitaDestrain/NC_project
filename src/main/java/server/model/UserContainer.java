package server.model;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name = "userContainer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({User.class})
public class UserContainer {
    @XmlElement(name = "users")
    private Map<Integer, User> users;

    public UserContainer() {
        users = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void removeUser(User user) {
        users.remove(user.getId());
    }

    public void removeUser(int id) {
        users.remove(id);
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public User getUserByLogin(String login) {
        for (User user : getUsers()) {
            if (user.getLogin().equals(login))
                return user;
        }
        return null;
    }

    public void setRole(int id, String role) {
        users.get(id).setRole(role);
    }

    public List<User> getUsers() {
        LinkedList<User> list = new LinkedList<>(users.values());
        return Collections.unmodifiableList(list);
    }
}

