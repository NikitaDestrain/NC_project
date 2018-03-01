package server.factories;

import server.controller.IDGenerator;
import server.model.Task;
import server.model.TaskStatus;

import java.util.Date;

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
    public static Task createTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate,
                                  Date addingDate, Date updatingDate, int journalId) {
        return new Task(name, status, description, notificationDate, plannedDate, addingDate, updatingDate, journalId, IDGenerator.getInstance().createId());
    }

    public static Task createTask(Task task) {
        return new Task(task.getName(), task.getStatus(), task.getDescription(), task.getNotificationDate(),
                task.getPlannedDate(), task.getAddingDate(), task.getUpdatingDate(), task.getJournalId(), IDGenerator.getInstance().createId());
    }
}
