package database;

import database.daointerfaces.TasksDAO;
import server.model.Task;
import server.model.TaskStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PostgreSQLTasksDAO implements TasksDAO {
    private final Connection connection;

    public PostgreSQLTasksDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate) {
        return null;
    }

    @Override
    public Task read(int id) {
        return null;
    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Task> getAll() throws SQLException {
        return null;
    }
}
