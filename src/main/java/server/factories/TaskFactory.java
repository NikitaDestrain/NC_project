package server.factories;

import server.model.Task;
import server.model.TaskStatus;

import java.sql.Date;

public class TaskFactory {
    /**
     * Method for creating a task object
     * {@linkplain Task}
     *
     * @param name             task name
     * @param status           task status
     * @param description      task description
     * @param notificationDate task notification Date
     * @param plannedDate      task plannedDate
     * @return new object <b> Task </b> with generated id
     */
    public static Task createTask(int id, String name, String status, String description, Date notificationDate, Date plannedDate,
                                  Date uploadDate, Date changeDate, int journalId) {
        return new Task(id, name, parseStringToTaskStatus(status), description, notificationDate, plannedDate,
                uploadDate, changeDate, journalId);
    }

    public static Task createTask(String name, String status, String description, Date notificationDate,
                                  Date plannedDate, int journalId) {
        return new Task(name, parseStringToTaskStatus(status), description, notificationDate, plannedDate,
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), journalId);
    }

    public static Task createTask(Task task) {
        return new Task(task.getId(), task.getName(), task.getStatus(), task.getDescription(), task.getNotificationDate(),
                task.getPlannedDate(), task.getUploadDate(), task.getChangeDate(), task.getJournalId());
    }

    private static TaskStatus parseStringToTaskStatus(String status) {
        switch (status) {
            case "Planned":
                return TaskStatus.Planned;
            case "Completed":
                return TaskStatus.Completed;
            case "Rescheduled":
                return TaskStatus.Rescheduled;
            case "Cancelled":
                return TaskStatus.Cancelled;
            case "Overdue":
                return TaskStatus.Overdue;
        }
        return null;
    }
}
