package database.hibernate;

import database.daointerfaces.UsersDAO;
import org.hibernate.Session;
import server.factories.UserFactory;
import server.model.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HibernateUsersDAO implements UsersDAO {

    @Override
    public User create(String login, String password, String role) throws SQLException {
        Session session = null;
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
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public User read(int id) throws SQLException {
        Session session = null;
        User user;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (User) session.load(User.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(user);
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
    public void delete(User user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
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
    public List<User> getAll() throws SQLException {
        Session session = null;
        List<User> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(User.class).list();
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
    public User getByLoginAndPassword(String login, String password) throws SQLException {
        return null;
    }

    @Override
    public List<User> getSortedByCriteria(String column, String criteria) throws SQLException {
        return null;
    }
}
