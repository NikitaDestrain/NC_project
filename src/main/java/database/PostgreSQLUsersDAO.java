package database;

import database.daointerfaces.UsersDAO;
import server.commandproccessor.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PostgreSQLUsersDAO implements UsersDAO {
    private final Connection connection;

    public PostgreSQLUsersDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public User create(String login, String password, String role) {
        return null;
    }

    @Override
    public User read(int id) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<User> getAll() throws SQLException {
        return null;
    }
}
