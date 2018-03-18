package database.postgresql;

import database.daointerfaces.UsersDAO;
import server.factories.UserFactory;
import server.model.User;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLUsersDAO implements UsersDAO {
    private final Connection connection;

    public PostgreSQLUsersDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(String login, String password, String role) throws SQLException {
        String sql = "INSERT INTO \"Users\" (\"Login\", \"Password\", \"Role\", " +
                "\"Registration_date\") VALUES (? , ?, ?, ?);";
        String sqlSelect = "SELECT * FROM \"Users\" WHERE \"Login\" = ?";
        User user;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, login);
            stm.setString(2, password);
            stm.setString(3, role);
            stm.setDate(4, new Date(System.currentTimeMillis()));
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, login);
                user = createUserByResultSet(stm2.executeQuery());
            }
        }
        return user;
    }

    @Override
    public User read(int id) throws SQLException {
        String sql = "SELECT * FROM \"Users\" WHERE id = ?;";
        User user;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            user = createUserByResultSet(stm.executeQuery());
        }
        return user;
    }

    @Override
    public User getByLoginAndPassword(String login, String password) throws SQLException {
        String sql = "SELECT * FROM \"Users\" WHERE \"Login\" = ? AND \"Password\" = ?;";
        User user;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, login);
            stm.setString(2, password);
            user = createUserByResultSet(stm.executeQuery());
        }
        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE \"Users\" SET \"Login\" = ?, \"Password\" = ?, \"Role\" = ? WHERE \"User_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setString(1, user.getLogin());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getRole());
            stm.setInt(4, user.getId());
            stm.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM \"Users\" WHERE \"User_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setInt(1, id);
            stm.executeUpdate();
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> list;
        String sql = "SELECT * FROM \"Users\"";
        System.out.println(connection);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            list = createListByResultSet(statement.executeQuery());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<User> getSortedByCriteria(String column, String criteria) throws SQLException {
        List<User> list;
        String sql = "SELECT * FROM \"Users\" ORDER BY \"%s\" %s";
        sql = String.format(sql, column, criteria);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            list = createListByResultSet(statement.executeQuery());
        }
        return Collections.unmodifiableList(list);
    }

    private User createUserByResultSet(ResultSet rs) throws SQLException {
        rs.next();
        return UserFactory.createUser(rs.getInt("User_id"), rs.getString("Login"),
                rs.getString("Password"), rs.getString("Role"), rs.getDate("Registration_date"));
    }

    private List<User> createListByResultSet(ResultSet rs) throws SQLException {
        List<User> list = new LinkedList<>();
        while (rs.next()) {
            list.add(UserFactory.createUser(rs.getInt("User_id"), rs.getString("Login"),
                    rs.getString("Password"), rs.getString("Role"), rs.getDate("Registration_date")));
        }
        return list;
    }
}
