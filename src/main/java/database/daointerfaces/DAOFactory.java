package database.daointerfaces;

public interface DAOFactory {
    public TasksDAO getTasksDao();

    public JournalDAO getJournalDao();

    public UsersDAO getUsersDao();
}
