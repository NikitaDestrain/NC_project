package database.daointerfaces;

import server.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UsersDAO {
    public User create(String login, String password, String role) throws SQLException;

    public User read(int id) throws SQLException;

    public void update(User user) throws SQLException;

    public void delete(int id) throws SQLException;

    public List<User> getAll() throws SQLException;

    public List<User> getSortedByAscending(String column) throws SQLException;

    public List<User> getSortedByDescending(String column) throws SQLException;
}
