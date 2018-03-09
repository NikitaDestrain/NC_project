package database.daointerfaces;

import server.model.Task;
import server.model.TaskStatus;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public interface TasksDAO {
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException;

    public Task read(int id) throws SQLException;

    public void update(Task task) throws SQLException;

    public void delete(int id) throws SQLException;

    public List<Task> getAll() throws SQLException;

    public List<Task> getSortedByCriteria(String column, String criteria) throws SQLException;

    public List<Task> getFilteredByPattern(String column, String pattern, String criteria) throws SQLException;

    public List<Task> getFilteredByEquals(String column, String equal, String criteria) throws SQLException;
}