package database.daointerfaces;

import server.commandproccessor.User;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface UsersDAO {
    public User create(String login, String password, String role);

    public User read(int id);

    public void update(User user);

    public void delete(int id);

    public List<User> getAll() throws SQLException;
}
