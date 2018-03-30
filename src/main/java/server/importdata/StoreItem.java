package server.importdata;

import server.model.Journal;
import server.model.JournalContainer;

public class StoreItem {
    private final String type;
    private String strategy;
    private Journal journal = null;
    private JournalContainer container = null;

    public StoreItem(String type, Journal journal) {
        this.type = type;
        this.journal = journal;
    }

    public StoreItem(String type, JournalContainer container) {
        this.type = type;
        this.container = container;
    }

    public String getType() {
        return type;
    }


    public Journal getJournal() {
        return journal;
    }

    public JournalContainer getContainer() {
        return container;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
