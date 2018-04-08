package database.hibernate;

import auxiliaryclasses.ConstantsClass;
import database.daointerfaces.UsersDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import server.factories.UserFactory;
import server.model.User;

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
            user = UserFactory.createUser(login, password, role);
            session.save(user);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException();
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
            user = (User) session.get(User.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException();
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
            throw new SQLException();
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
            throw new SQLException();
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
            throw new SQLException();
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public User getByLoginAndPassword(String login, String password) throws SQLException {
        User user;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria equalResult = session.createCriteria(User.class)
                    .add(Restrictions.eq(ConstantsClass.LOGIN_PARAMETER, login))
                    .add(Restrictions.eq(ConstantsClass.PASSWORD_PARAMETER, password));
            user = (User) equalResult.list().get(0);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException();
        } finally {
            finishSession();
        }
        return user;
    }

    @Override
    public List<User> getSortedByCriteria(String column, String criteria) throws SQLException {
        List<User> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = getOrderCriteria(column, criteria, session.createCriteria(User.class)).list();
        } catch (ExceptionInInitializerError e) {
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

    private Criteria getOrderCriteria(String column, String criteria, Criteria resultCriteria) {
        if (criteria.equalsIgnoreCase(ConstantsClass.SORT_ASC))
            return resultCriteria.addOrder(Order.asc(column));
        if (criteria.equalsIgnoreCase(ConstantsClass.SORT_DESC))
            return resultCriteria.addOrder(Order.desc(column));
        return null;
    }
}
