package server.exportdata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExportList {
    private List<Integer> journalIds;
    private List<Integer> taskIds;

    public ExportList() {
        journalIds = new LinkedList<>();
        taskIds = new LinkedList<>();
    }

    public ExportList(List<Integer> journalIds, List<Integer> taskIds) {
        this.journalIds = journalIds;
        this.taskIds = taskIds;
    }

    public List<Integer> getJournalIds() {
        return Collections.unmodifiableList(journalIds);
    }

    public List<Integer> getTaskIds() {
        return Collections.unmodifiableList(taskIds);
    }

    public void addJournalId(int journalId) {
        this.journalIds.add(journalId);
    }

    public void addTaskId(int taskId) {
        this.taskIds.add(taskId);
    }
}
