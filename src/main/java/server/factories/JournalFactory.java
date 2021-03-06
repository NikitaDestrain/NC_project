package server.factories;

import server.model.Journal;

public class JournalFactory {

    public static Journal createJournal(int id, String name, String description, Integer userId) {
        return new Journal(id, name, description, userId);
    }

    public static Journal createJournal(String name, String description, Integer userId) {
        return new Journal(name, description, userId);
    }

    public static Journal createJournal(Journal journal) {
        return new Journal(journal.getId(), journal.getName(), journal.getDescription(), journal.getUserId());
    }
}
