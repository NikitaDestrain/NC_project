package testservlet.beans;

import java.sql.ResultSet;
import java.util.LinkedList;

public class SelectResultBean {
    private LinkedList<User> users;

    public SelectResultBean() {

    }

    public SelectResultBean(LinkedList<User> users) {
        this.users = users;
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    public void setUsers(LinkedList<User> users) {
        this.users = users;
    }
}
