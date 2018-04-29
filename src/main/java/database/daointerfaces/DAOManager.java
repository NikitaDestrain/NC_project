package database.daointerfaces;

public interface DAOManager {
    public TasksDAO getTasksDao();

    public JournalDAO getJournalDao();

    public UsersDAO getUsersDao();
}
