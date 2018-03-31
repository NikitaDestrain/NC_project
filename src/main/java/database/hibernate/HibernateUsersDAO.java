package database.hibernate;

import database.daointerfaces.UsersDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import server.factories.UserFactory;
import server.model.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HibernateUsersDAO implements UsersDAO {

    private Session session;

    @Override
    public User create(String login, String password, String role) throws SQLException {
        User user;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            user = UserFactory.createUser(login, password, role, new Date(System.currentTimeMillis()));
            session.save(user);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! ADD");
        } finally {
            finishSession();
        }
        return user;
    }

    @Override
    public User read(int id) throws SQLException {
        User user;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (User) session.load(User.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ");
        } finally {
            finishSession();
        }
        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! UPDATE");
        } finally {
            finishSession();
        }
    }

    @Override
    public void delete(User user) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! DELETE");
        } finally {
            finishSession();
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(User.class).list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ ALL");
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public User getByLoginAndPassword(String login, String password) throws SQLException {
        String sql = "SELECT * FROM \"Users\" WHERE \"Login\" = %s AND \"Password\" = %s;";
        sql = String.format(sql, login, password);
        Query query = session.createQuery(sql);
        return (User) query.list().get(0);
    }

    @Override
    public List<User> getSortedByCriteria(String column, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Users\" ORDER BY \"%s\" %s";
        sql = String.format(sql, column, criteria);
        return Collections.unmodifiableList(getListByQuery(sql));
    }

    private void finishSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private List<User> getListByQuery(String sql) throws SQLException {
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
