package database.daointerfaces;

import server.model.Task;
import server.model.TaskStatus;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface TasksDAO {
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate);

    public Task read(int id);

    public void update(Task task);

    public void delete(int id);

    public List<Task> getAll() throws SQLException;
}
