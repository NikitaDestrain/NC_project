package server.importdata;

import server.model.Journal;
import server.model.JournalContainer;

public class StoreItem {
    private String journalStrategy;
    private String taskStrategy;
    private Journal journal = null;
    private JournalContainer container = null;

    public StoreItem(Journal journal) {
        this.journal = journal;
    }

    public StoreItem(JournalContainer container) {
        this.container = container;
    }

    public Journal getJournal() {
        return journal;
    }

    public JournalContainer getContainer() {
        return container;
    }

    public String getJournalStrategy() {
        return journalStrategy;
    }

    public void setJournalStrategy(String journalStrategy) {
        this.journalStrategy = journalStrategy;
    }

    public String getTaskStrategy() {
        return taskStrategy;
    }

    public void setTaskStrategy(String taskStrategy) {
        this.taskStrategy = taskStrategy;
    }
}
