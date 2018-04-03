package database.hibernate;

import database.daointerfaces.TasksDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import server.factories.TaskFactory;
import server.model.Task;
import server.model.TaskStatus;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HibernateTasksDAO implements TasksDAO {

    private Session session;

    @Override
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        Task task;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            task = TaskFactory.createTask(name, status.toString(), description, notificationDate, plannedDate, journalId);
            session.save(task);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! ADD");
        } finally {
            finishSession();
        }
        return task;
    }

    @Override
    public Task read(int id) throws SQLException {
        Task task;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            task = (Task) session.load(Task.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ");
        } finally {
            finishSession();
        }
        return task;
    }

    @Override
    public void update(Task task) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! UPDATE");
        } finally {
            finishSession();
        }
    }

    @Override
    public void delete(Task task) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(task);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! DELETE");
        } finally {
            finishSession();
        }
    }

    @Override
    public List<Task> getAll() throws SQLException {
        List<Task> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(Task.class).list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ ALL");
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getSortedByCriteria(int journalId, String column, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Tasks\" WHERE \"Journal_id\" = ? ORDER BY \"%s\" %s";
        sql = String.format(sql, column, criteria);
        return Collections.unmodifiableList(getListByQuery(sql));
    }

    @Override
    public List<Task> getFilteredByPattern(int journalId, String column, String pattern, String criteria) throws
            SQLException {
        String sql = "SELECT * FROM \"Tasks\" WHERE \"%s\" LIKE \'%s%s%s\' AND \"Journal_id\" = ? ORDER BY \"%s\" %s";
        sql = String.format(sql, column, '%', pattern, '%', column, criteria);
        return Collections.unmodifiableList(getListByQuery(sql));
    }

    @Override
    public List<Task> getFilteredByEquals(int journalId, String column, String equal, String criteria) throws
            SQLException {
        String sql = "SELECT * FROM \"Tasks\" WHERE \"%s\"::text = \'%s\' AND \"Journal_id\" = ? ORDER BY \"%s\" %s";
        sql = String.format(sql, column, equal, column, criteria);
        return Collections.unmodifiableList(getListByQuery(sql));
    }

    private void finishSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private List<Task> getListByQuery(String sql) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(sql);
            return query.list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ SORT");
        } finally {
            finishSession();
        }
    }

}
