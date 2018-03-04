package servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Account {
    private Connection connection;
    private static Account instance;

    private Account(Connection connection) {
        this.connection = connection;
    }

    public static Account getInstance(Connection connection) {
        if (instance == null) instance = new Account(connection);
        return instance;
    }

    public boolean login(String login, String password) {
        try {
            //String query = "select count(*) as count from users where email='" + login +"'" + "AND password='" + password +"'";
            String query = "SELECT count(*) as count from users where email=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet set = preparedStatement.executeQuery();

            int count = 0;
            while (set.next()) {
                count = set.getInt("count");
            }
            return count != 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isSuchUserExists(String email) {
        String query = "select count(*) as count from users where email=" + "'" + email +"'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            //statement.setString(1, email);

            ResultSet set = statement.executeQuery();
            int count = 0;

            if (set.next()) {
                count = set.getInt("count");
            }

            return !(count == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createUser(String email, String password) {
        String query = "insert into users(id, email, password) values(?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, new Random().nextInt(Integer.MAX_VALUE));
            statement.setString(2, email);
            statement.setString(3, password);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
