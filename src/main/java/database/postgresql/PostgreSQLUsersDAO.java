package database.postgresql;

import database.daointerfaces.UsersDAO;
import server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "\"Registration_date\") VALUES (? , ?, ?, sysdate);";
        String sqlSelect = "SELECT * FROM \"Users\" WHERE \"Login\" = ?";
        User user = new User();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, login);
            stm.setString(2, password);
            stm.setString(3, role);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, login);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                user.setId(rs2.getInt("User_id"));
                user.setLogin(rs2.getString("Login"));
                user.setPassword(rs2.getString("Password"));
                user.setRole(rs2.getString("Role"));
                user.setRegistrationDate(rs2.getDate("Registration_date"));
            }
        }
        return user;
    }

    @Override
    public User read(int id) throws SQLException {
        String sql = "SELECT * FROM \"Users\" WHERE id = ?;";
        User user = new User();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            rs.next();
            user.setId(rs.getInt("User_id"));
            user.setLogin(rs.getString("Login"));
            user.setPassword(rs.getString("Password"));
            user.setRole(rs.getString("Role"));
            user.setRegistrationDate(rs.getDate("Registration_date"));
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
        List<User> list = new LinkedList<>();
        String sql = "SELECT * FROM \"Journal\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("User_id"));
                user.setLogin(rs.getString("Login"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));
                user.setRegistrationDate(rs.getDate("Registration_date"));
                list.add(user);
            }
        }
        return Collections.unmodifiableList(list);
    }
}
