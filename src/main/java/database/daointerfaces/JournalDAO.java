package database.daointerfaces;

import server.model.Journal;

import java.sql.SQLException;
import java.util.List;

public interface JournalDAO {
    public Journal create(String name, String description);

    public Journal read(int id);

    public void update(Journal journal);

    public void delete(int id);

    public List<Journal> getAll() throws SQLException;
}
