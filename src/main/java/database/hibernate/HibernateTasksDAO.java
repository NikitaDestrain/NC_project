package database.hibernate;

import auxiliaryclasses.ConstantsClass;
import database.daointerfaces.TasksDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return task;
    }

    public Task create(int id, String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, Integer journalId) throws SQLException {
        Task task;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            task = TaskFactory.createTask(id, name, status.toString(), description, notificationDate, plannedDate, journalId);
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new SQLException();
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
            task = (Task) session.get(Task.class, id);
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return task;
    }

    public boolean contains(int id) {
        try {
            return read(id) != null;
        } catch (Exception e) {
            return false;
        } finally {
            finishSession();
        }
    }

    public boolean contains(String name) {
        try {
            return read(name) != null;
        } catch (Exception e) {
            return false;
        } finally {
            finishSession();
        }
    }

    public Task read(String name) throws SQLException {
        Task task;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            task = (Task) session.get(Task.class, name);
        } catch (Exception e) {
            throw new SQLException();
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
        } catch (Exception e) {
            throw new SQLException();
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
        } catch (Exception e) {
            throw new SQLException();
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
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getSortedByCriteria(int journalId, String column, String criteria) throws SQLException {
        List<Task> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = getOrderCriteria(journalId, column, criteria, session.createCriteria(Task.class)).list();
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getFilteredByPattern(int journalId, String column, String pattern, String criteria) throws
            SQLException {
        List<Task> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria likeResult = session.createCriteria(Task.class)
                    .add(Restrictions.like(column, createPattern(pattern)));
            list = getOrderCriteria(journalId, column, criteria, likeResult).list();
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getFilteredByEquals(int journalId, String column, String equal, String criteria) throws
            SQLException {
        List<Task> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria equalResult = session.createCriteria(Task.class)
                    .add(Restrictions.eq(column, equal));
            list = getOrderCriteria(journalId, column, criteria, equalResult).list();
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    private void finishSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private Criteria getOrderCriteria(int journalId, String column, String criteria, Criteria resultCriteria) {
        resultCriteria.add(Restrictions.eq(ConstantsClass.HIBERNATE_JOURNAL_ID, journalId));
        if (criteria.equalsIgnoreCase(ConstantsClass.SORT_ASC))
            return resultCriteria.addOrder(Order.asc(column));
        if (criteria.equalsIgnoreCase(ConstantsClass.SORT_DESC))
            return resultCriteria.addOrder(Order.desc(column));
        return null;
    }

    private String createPattern(String pattern) {
        return "%" + pattern + "%";
    }
}
