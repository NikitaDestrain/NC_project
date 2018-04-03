package database.hibernate;

import auxiliaryclasses.ConstantsClass;
import database.daointerfaces.JournalDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import server.factories.JournalFactory;
import server.model.Journal;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HibernateJournalDAO implements JournalDAO {

    private Session session;

    @Override
    public Journal create(String name, String description, Integer userId) throws SQLException {
        Journal journal;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            journal = JournalFactory.createJournal(name, description, userId);
            session.save(journal);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! ADD");
        } finally {
            finishSession();
        }
        return journal;
    }

    @Override
    public Journal read(int id) throws SQLException {
        Journal journal;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            journal = (Journal) session.load(Journal.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ");
        } finally {
            finishSession();
        }
        return journal;
    }

    @Override
    public void update(Journal journal) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(journal);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! UPDATE");
        } finally {
            finishSession();
        }
    }

    @Override
    public void delete(Journal journal) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(journal);
            session.getTransaction().commit();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! DELETE");
        } finally {
            finishSession();
        }
    }

    @Override
    public List<Journal> getAll() throws SQLException {
        List<Journal> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(Journal.class).list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ ALL");
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Journal> getSortedByCriteria(String column, String criteria) throws SQLException {
        List<Journal> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = getOrderCriteria(column, criteria, session.createCriteria(Journal.class)).list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ SORT");
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Journal> getFilteredByPattern(String column, String pattern, String criteria) throws SQLException {
        List<Journal> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria likeResult = session.createCriteria(Journal.class)
                    .add(Restrictions.like(column, createPattern(pattern)));
            list = getOrderCriteria(column, criteria, likeResult).list();
        } catch (Exception e) {
            throw new SQLException("Error! READ SORT PATTERN");
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Journal> getFilteredByEquals(String column, String equal, String criteria) throws SQLException {
        List<Journal> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria equalResult = session.createCriteria(Journal.class)
                    .add(Restrictions.eq(column, equal));
            list = getOrderCriteria(column, criteria, equalResult).list();
        } catch (Exception e) {
            throw new SQLException("Error! READ SORT EQUAL");
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

    private String createPattern(String pattern) {
        return "%" + pattern + "%";
    }
}
