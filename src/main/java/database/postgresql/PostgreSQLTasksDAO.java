package database.postgresql;

import database.daointerfaces.TasksDAO;
import server.factories.TaskFactory;
import server.model.Task;
import server.model.TaskStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLTasksDAO implements TasksDAO {
    private final Connection connection;

    public PostgreSQLTasksDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        String sql = "INSERT INTO \"Tasks\" (\"Name\", \"Status\", \"Description\", " +
                "\"Planned_date\", \"Notification_date\", \"Upload_date\"," +
                " \"Change_date\", \"Journal_id\") VALUES (? , ?, ?, ?, ?, ?, ?, ?);";
        String sqlSelect = "SELECT * FROM \"Tasks\" WHERE \"Name\" = ?";
        Task task;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, status.toString());
            stm.setString(3, description);
            stm.setDate(4, plannedDate);
            stm.setDate(5, notificationDate);
            stm.setDate(6, new Date(System.currentTimeMillis()));
            stm.setDate(7, new Date(System.currentTimeMillis()));
            stm.setInt(8, journalId);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, name);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                task = TaskFactory.createTask(rs2.getInt("Task_id"), rs2.getString("Name"),
                        rs2.getString("Status"), rs2.getString("Description"),
                        rs2.getDate("Notification_date"), rs2.getDate("Planned_date"),
                        rs2.getDate("Upload_date"), rs2.getDate("Change_date"),
                        rs2.getInt("Journal_id"));
            }
        }
        return task;
    }

    @Override
    public Task read(int id) throws SQLException {
        String sql = "SELECT * FROM \"Tasks\" WHERE id = ?;";
        Task task;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            rs.next();
            task = TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                    rs.getString("Status"), rs.getString("Description"),
                    rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                    rs.getDate("Upload_date"), rs.getDate("Change_date"),
                    rs.getInt("Journal_id"));
        }
        return task;
    }

    @Override
    public void update(Task task) throws SQLException {
        String sql = "UPDATE \"Tasks\" SET \"Name\" = ?, \"Status\" = ?, \"Description\" = ?, " +
                "\"Planned_date\" = ?, \"Notification_date\" = ?, \"Upload_date\" = ?, " +
                "\"Change_date\" = ?, \"Journal_id\" = ? WHERE \"Task_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setString(1, task.getName());
            stm.setString(2, task.getStatus().toString());
            stm.setString(3, task.getDescription());
            stm.setDate(4, task.getPlannedDate());
            stm.setDate(5, task.getNotificationDate());
            stm.setDate(6, task.getUploadDate());
            stm.setDate(7, new Date(System.currentTimeMillis()));
            stm.setInt(8, task.getJournalId());
            stm.setInt(9, task.getId());
            stm.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM \"Tasks\" WHERE \"Task_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setInt(1, id);
            stm.executeUpdate();
        }
    }

    @Override
    public List<Task> getAll() throws SQLException {
        List<Task> list = new LinkedList<>();
        String sql = "SELECT * FROM \"Tasks\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                        rs.getString("Status"), rs.getString("Description"),
                        rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                        rs.getDate("Upload_date"), rs.getDate("Change_date"),
                        rs.getInt("Journal_id")));
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getSortedByCriteria(String column, String criteria) throws SQLException {
        List<Task> list = new LinkedList<>();
        String sql = "SELECT * FROM \"Tasks\" ORDER BY ? ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, column);
            statement.setString(2, criteria.toUpperCase());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                        rs.getString("Status"), rs.getString("Description"),
                        rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                        rs.getDate("Upload_date"), rs.getDate("Change_date"),
                        rs.getInt("Journal_id")));
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getFilteredByPattern(String column, String pattern, String criteria) throws SQLException {
        List<Task> list = new LinkedList<>();
        String sql = "SELECT * FROM \"Tasks\" WHERE ? LIKE ? ORDER BY ? ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, column);
            statement.setObject(2, pattern);
            statement.setString(3, column);
            statement.setString(4, criteria.toUpperCase());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                        rs.getString("Status"), rs.getString("Description"),
                        rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                        rs.getDate("Upload_date"), rs.getDate("Change_date"),
                        rs.getInt("Journal_id")));
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getFilteredByEquals(String column, String equal, String criteria) throws SQLException {
        List<Task> list = new LinkedList<>();
        String sql = "SELECT * FROM \"Tasks\" WHERE ? = ? ORDER BY ? ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, column);
            statement.setObject(2, equal);
            statement.setString(3, column);
            statement.setString(4, criteria.toUpperCase());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                        rs.getString("Status"), rs.getString("Description"),
                        rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                        rs.getDate("Upload_date"), rs.getDate("Change_date"),
                        rs.getInt("Journal_id")));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
