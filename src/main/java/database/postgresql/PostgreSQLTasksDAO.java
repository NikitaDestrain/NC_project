package database.postgresql;

import database.daointerfaces.TasksDAO;
import server.model.Task;
import server.model.TaskStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLTasksDAO implements TasksDAO {
    private final Connection connection;

    public PostgreSQLTasksDAO(Connection connection) {
        this.connection = connection;
    }

    //todo протестировать с датой, так как разные типы (возможно поменять все типы на один) + исправить скрипт добавить в таблицу статус задачи
    @Override
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        String sql = "INSERT INTO \"Tasks\" (\"Name\", \"Description\", " +
                "\"Planned_date\", \"Notification_date\", \"Upload_date\"," +
                " \"Change_date\", \"Journal_id\") VALUES (? , ?, ?, ?, sysdate, sysdate, ?);";
        String sqlSelect = "SELECT * FROM \"Tasks\" WHERE \"Name\" = ?";
        Task task = new Task();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, description);
            stm.setDate(3, (java.sql.Date) plannedDate);
            stm.setDate(4, (java.sql.Date) notificationDate);
            stm.setInt(5, journalId);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, name);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                task.setId(rs2.getInt("Task_id"));
                task.setName(rs2.getString("Name"));
                task.setDescription(rs2.getString("Description"));
                task.setPlannedDate(rs2.getDate("Planned_date"));
                task.setNotificationDate(rs2.getDate("Notification_date"));
                task.setUploadDate(rs2.getDate("Upload_date"));
                task.setChangeDate(rs2.getDate("Change_date"));
                task.setJournalId(rs2.getInt("Journal_id"));
            }
        }
        return task;
    }

    @Override
    public Task read(int id) throws SQLException {
        String sql = "SELECT * FROM \"Tasks\" WHERE id = ?;";
        Task task = new Task();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            rs.next();
            task.setId(rs.getInt("Task_id"));
            task.setName(rs.getString("Name"));
            task.setDescription(rs.getString("Description"));
            task.setPlannedDate(rs.getDate("Planned_date"));
            task.setNotificationDate(rs.getDate("Notification_date"));
            task.setUploadDate(rs.getDate("Upload_date"));
            task.setChangeDate(rs.getDate("Change_date"));
            task.setJournalId(rs.getInt("Journal_id"));
        }
        return task;
    }

    @Override
    public void update(Task task) throws SQLException {
        String sql = "UPDATE \"Tasks\" SET \"Name\" = ?, \"Description\" = ?, " +
                "\"Planned_date\" = ?, \"Notification_date\" = ?, \"Upload_date\" = ?, " +
                "\"Change_date\" = ?, \"Journal_id\" = ? WHERE \"Task_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setString(1, task.getName());
            stm.setString(2, task.getDescription());
            stm.setDate(3, (java.sql.Date) task.getPlannedDate());
            stm.setDate(4, (java.sql.Date) task.getNotificationDate());
            stm.setDate(5, (java.sql.Date) task.getUploadDate());
            stm.setDate(6, (java.sql.Date) task.getChangeDate());
            stm.setInt(7, task.getJournalId());
            stm.setInt(8, task.getId());
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
        String sql = "SELECT * FROM \"Journal\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("Task_id"));
                task.setName(rs.getString("Name"));
                task.setDescription(rs.getString("Description"));
                task.setPlannedDate(rs.getDate("Planned_date"));
                task.setNotificationDate(rs.getDate("Notification_date"));
                task.setUploadDate(rs.getDate("Upload_date"));
                task.setChangeDate(rs.getDate("Change_date"));
                task.setJournalId(rs.getInt("Journal_id"));
                list.add(task);
            }
        }
        return Collections.unmodifiableList(list);
    }
}
