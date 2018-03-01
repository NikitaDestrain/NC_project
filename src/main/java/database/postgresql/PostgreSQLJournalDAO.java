package database.postgresql;

import database.daointerfaces.JournalDAO;
import server.model.Journal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PostgreSQLJournalDAO implements JournalDAO {
    private final Connection connection;

    public PostgreSQLJournalDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Journal create(String name, String description) {
        return null;
    }

    @Override
    public Journal read(int id) {
        return null;
    }

    @Override
    public void update(Journal journal) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Journal> getAll() throws SQLException {
        return null;
    }
}
