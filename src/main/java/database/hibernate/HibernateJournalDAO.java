package database.hibernate;

import database.daointerfaces.JournalDAO;
import org.hibernate.Session;
import server.factories.JournalFactory;
import server.model.Journal;
import server.model.Task;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HibernateJournalDAO implements JournalDAO {
    @Override
    public Journal create(String name, String description, Integer userId) throws SQLException {
        Session session = null;
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
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return journal;
    }

    @Override
    public Journal read(int id) throws SQLException {
        Session session = null;
        Journal journal;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            journal = (Journal) session.load(Journal.class, id);
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ");
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return journal;
    }

    @Override
    public void update(Journal journal) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(journal);
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
    public void delete(Journal journal) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(journal);
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
    public List<Journal> getAll() throws SQLException {
        Session session = null;
        List<Journal> list;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createCriteria(Journal.class).list();
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
    public List<Journal> getSortedByCriteria(String column, String criteria) throws SQLException {
        return null;
    }

    @Override
    public List<Journal> getFilteredByPattern(String column, String pattern, String criteria) throws SQLException {
        return null;
    }

    @Override
    public List<Journal> getFilteredByEquals(String column, String equal, String criteria) throws SQLException {
        return null;
    }
}
