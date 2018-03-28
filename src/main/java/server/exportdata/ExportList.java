package server.exportdata;

import java.util.List;

public class ExportList {
    private List<Integer> journalIds;
    private List<Integer> taskIds;

    public ExportList() {}

    public ExportList(List<Integer> journalIds, List<Integer> taskIds) {
        this.journalIds = journalIds;
        this.taskIds = taskIds;
    }

    public List<Integer> getJournalIds() {
        return journalIds;
    }

    public void setJournalIds(List<Integer> journalIds) {
        this.journalIds = journalIds;
    }

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }

    public void addJournalId(int journalId) {
        this.journalIds.add(journalId);
    }

    public void addTaskId(int taskId) {
        this.taskIds.add(taskId);
    }
}
