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

    public void delete(Task task) throws SQLException;

    public List<Task> getAll() throws SQLException;

    public List<Task> getSortedByCriteria(int journalId, String column, String criteria) throws SQLException;

    public List<Task> getFilteredByPattern(int journalId, String column, String pattern, String criteria) throws SQLException;

    public List<Task> getFilteredByEquals(int journalId, String column, String equal, String criteria) throws SQLException;
}
