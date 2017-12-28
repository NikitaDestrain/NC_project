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
    public static Task createTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate) {
        return new Task(name, status, description, notificationDate, plannedDate, IDGenerator.getInstance().createId());
    }
}
