package database.hibernate;

import database.daointerfaces.TasksDAO;
import org.hibernate.Session;
import server.factories.TaskFactory;
import server.model.Task;
import server.model.TaskStatus;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HibernateTasksDAO implements TasksDAO {
    @Override
    public Task create(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        Session session = null;
        Task task;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            task = TaskFactory.createTask(name, status.toString(), description, notificationDate, plannedDate,
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), journalId);
            session.save(task);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! ADD");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return task;
    }

    @Override
    public Task read(int id) throws SQLException {
        Session session = null;
        Task task;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            task = (Task) session.load(Task.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return task;
    }

    @Override
    public void update(Task task) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! UPDATE");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void delete(Task task) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(task);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! DELETE");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Task> getAll() throws SQLException {
        Session session = null;
        List<Task> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(Task.class).list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ ALL");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getSortedByCriteria(int journalId, String column, String criteria) throws SQLException {
        return null;
    }

    @Override
    public List<Task> getFilteredByPattern(int journalId, String column, String pattern, String criteria) throws
            SQLException {
        return null;
    }

    @Override
    public List<Task> getFilteredByEquals(int journalId, String column, String equal, String criteria) throws
            SQLException {
        return null;
    }
}
