package database.hibernate;

import auxiliaryclasses.ConstantsClass;
import database.daointerfaces.JournalDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
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
        List<Journal> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            if (criteria.equalsIgnoreCase(ConstantsClass.SORT_ASC))
                list = session.createCriteria(Journal.class)
                        .addOrder(Order.asc(column.toLowerCase()))
                        .list();
            if (criteria.equalsIgnoreCase(ConstantsClass.SORT_DESC))
                list = session.createCriteria(Journal.class)
                        .addOrder(Order.desc(column.toLowerCase()))
                        .list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ ALL");
        } finally {
            finishSession();
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Journal> getFilteredByPattern(String column, String pattern, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Journal\" WHERE \"%s\"::text LIKE \'%s%s%s\' ORDER BY \"%s\" %s";
        sql = String.format(sql, column, '%', pattern, '%', column, criteria);
        return Collections.unmodifiableList(getListByQuery(sql));
    }

    @Override
    public List<Journal> getFilteredByEquals(String column, String equal, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Journal\" WHERE \"%s\" = \'%s\' ORDER BY \"%s\" %s";
        sql = String.format(sql, column, equal, column, criteria);
        return Collections.unmodifiableList(getListByQuery(sql));
    }

    private void finishSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private List<Journal> getListByQuery(String sql) throws SQLException {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            System.out.println(sql);
            Query query = session.createQuery(sql);
            return query.list();
        } catch (ExceptionInInitializerError e) {
            throw new SQLException("Error! READ SORT");
        } finally {
            finishSession();
        }
    }
}
