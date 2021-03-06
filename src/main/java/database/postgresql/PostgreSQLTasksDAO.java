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
                task = createTaskByResultSet(stm2.executeQuery());
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
            task = createTaskByResultSet(stm.executeQuery());
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
    public void delete(Task task) throws SQLException {
        String sql = "DELETE FROM \"Tasks\" WHERE \"Task_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setInt(1, task.getId());
            stm.executeUpdate();
        }
    }

    @Override
    public List<Task> getAll() throws SQLException {
        List<Task> list;
        String sql = "SELECT * FROM \"Tasks\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            list = createListByResultSet(statement.executeQuery());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getSortedByCriteria(int journalId, String column, String criteria) throws SQLException {
        List<Task> list;
        String sql = "SELECT * FROM \"Tasks\" WHERE \"Journal_id\" = ? ORDER BY \"%s\" %s";
        sql = String.format(sql, column, criteria);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, journalId);
            list = createListByResultSet(statement.executeQuery());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getFilteredByPattern(int journalId, String column, String pattern, String criteria) throws SQLException {
        List<Task> list;
        String sql = "SELECT * FROM \"Tasks\" WHERE \"%s\" LIKE \'%s%s%s\' AND \"Journal_id\" = ? ORDER BY \"%s\" %s";
        sql = String.format(sql, column, '%', pattern, '%', column, criteria);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, journalId);
            list = createListByResultSet(statement.executeQuery());
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getFilteredByEquals(int journalId, String column, String equal, String criteria) throws SQLException {
        List<Task> list;
        String sql = "SELECT * FROM \"Tasks\" WHERE \"%s\"::text = \'%s\' AND \"Journal_id\" = ? ORDER BY \"%s\" %s";
        sql = String.format(sql, column, equal, column, criteria);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, journalId);
            list = createListByResultSet(statement.executeQuery());
        }
        return Collections.unmodifiableList(list);
    }

    private Task createTaskByResultSet(ResultSet rs) throws SQLException {
        rs.next();
        return TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                rs.getString("Status"), rs.getString("Description"),
                rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                rs.getDate("Upload_date"), rs.getDate("Change_date"),
                rs.getInt("Journal_id"));
    }

    private List<Task> createListByResultSet(ResultSet rs) throws SQLException {
        List<Task> list = new LinkedList<>();
        while (rs.next()) {
            list.add(TaskFactory.createTask(rs.getInt("Task_id"), rs.getString("Name"),
                    rs.getString("Status"), rs.getString("Description"),
                    rs.getDate("Notification_date"), rs.getDate("Planned_date"),
                    rs.getDate("Upload_date"), rs.getDate("Change_date"),
                    rs.getInt("Journal_id")));
        }
        return list;
    }
}
