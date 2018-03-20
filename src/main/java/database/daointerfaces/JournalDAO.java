package database.daointerfaces;

import server.model.Journal;

import java.sql.SQLException;
import java.util.List;

public interface JournalDAO {
    public Journal create(String name, String description, Integer userId) throws SQLException;

    public Journal read(int id) throws SQLException;

    public void update(Journal journal) throws SQLException;

    public void delete(int id) throws SQLException;

    public List<Journal> getAll() throws SQLException;

    public List<Journal> getSortedByCriteria(String column, String criteria) throws SQLException;
}
