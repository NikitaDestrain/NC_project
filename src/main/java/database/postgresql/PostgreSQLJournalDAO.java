package database.postgresql;

import database.daointerfaces.JournalDAO;
import server.factories.JournalFactory;
import server.model.Journal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLJournalDAO implements JournalDAO {
    private final Connection connection;

    public PostgreSQLJournalDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Journal create(String name, String description, Integer userId) throws SQLException {
        String sql = "INSERT INTO \"Journal\" (\"Name\", \"Description\", \"User_id\") VALUES (? , ?, ?);";
        String sqlSelect = "SELECT * FROM \"Journal\" WHERE \"Name\" = ?";
        Journal journal;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, description);
            stm.setInt(3, userId);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, name);
                journal = createJournalByResultSet(stm2.executeQuery());
            }
        }
        return journal;
    }

    @Override
    public Journal read(int id) throws SQLException {
        String sql = "SELECT * FROM \"Journal\" WHERE id = ?;";
        Journal journal;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            journal = createJournalByResultSet(stm.executeQuery());
        }
        return journal;
    }

    @Override
    public void update(Journal journal) throws SQLException {
        String sql = "UPDATE \"Journal\" SET \"Name\" = ?, \"Description\" = ?, " +
                "\"User_id\" = ? WHERE \"Journal_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setString(1, journal.getName());
            stm.setString(2, journal.getDescription());
            stm.setInt(3, journal.getUserId());
            stm.setInt(4, journal.getId());
            stm.executeUpdate();
        }
    }

    @Override
    public void delete(Journal journal) throws SQLException {
        String sql = "DELETE FROM \"Journal\" WHERE \"Journal_id\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql);) {
            stm.setInt(1, journal.getId());
            stm.executeUpdate();
        }
    }

    @Override
    public List<Journal> getAll() throws SQLException {
        String sql = "SELECT * FROM \"Journal\"";
        return Collections.unmodifiableList(createListByResultSet(sql));
    }

    @Override
    public List<Journal> getSortedByCriteria(String column, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Journal\" ORDER BY \"%s\" %s";
        sql = String.format(sql, column, criteria);
        return Collections.unmodifiableList(createListByResultSet(sql));
    }

    public List<Journal> getFilteredByPattern(String column, String pattern, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Journal\" WHERE \"%s\"::text LIKE \'%s%s%s\' ORDER BY \"%s\" %s";
        sql = String.format(sql, column, '%', pattern, '%', column, criteria);
        return Collections.unmodifiableList(createListByResultSet(sql));
    }

    public List<Journal> getFilteredByEquals(String column, String equal, String criteria) throws SQLException {
        String sql = "SELECT * FROM \"Journal\" WHERE \"%s\"::text = \'%s\' ORDER BY \"%s\" %s";
        sql = String.format(sql, column, equal, column, criteria);
        return Collections.unmodifiableList(createListByResultSet(sql));
    }

    private Journal createJournalByResultSet(ResultSet rs) throws SQLException {
        rs.next();
        return JournalFactory.createJournal(rs.getInt("Journal_id"), rs.getString("Name"),
                rs.getString("Description"), rs.getInt("User_id"));
    }

    private List<Journal> createListByResultSet(String sql) throws SQLException {
        List<Journal> list = new LinkedList<>();
        ResultSet rs;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            rs = statement.executeQuery();

            while (rs.next()) {
                list.add(JournalFactory.createJournal(rs.getInt("Journal_id"), rs.getString("Name"),
                        rs.getString("Description"), rs.getInt("User_id")));
            }
        }
        return list;
    }
}
