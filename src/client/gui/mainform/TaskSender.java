package client.gui.mainform;

import server.model.Task;

public class TaskSender {
    private Task task;
    private static TaskSender instance;

    private TaskSender() {
    }

    public static TaskSender getInstance() {
        if (instance == null)
            instance = new TaskSender();
        return instance;
    }

    public Task getTask() {
        return task;
    }

    public synchronized void setTask(Task task) {
        this.task = task;
    }

    public synchronized void clearTask() {
        task = null;
    }
}
