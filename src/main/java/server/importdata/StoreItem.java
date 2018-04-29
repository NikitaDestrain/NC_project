package server.importdata;

import server.model.Journal;
import server.model.Task;

import java.util.Collections;
import java.util.List;

public class StoreItem {
    private String journalStrategy;
    private String taskStrategy;
    private List<Journal> journals;
    private List<Task> tasks;

    public StoreItem(List<Journal> journals, List<Task> tasks) {
        this.journals = journals;
        this.tasks = tasks;
    }

    public StoreItem() {
    }

    public List<Journal> getJournals() {
        return Collections.unmodifiableList(journals);
    }

    public void setJournals(List<Journal> journals) {
        this.journals = journals;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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

    @Override
    public String toString() {
        return "StoreItem{" +
                "journals=" + journals +
                '}';
    }
}
