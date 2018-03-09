package server.controllerforweb;

import server.model.Task;
import server.model.TaskStatus;

import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private Task task;

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        if(task.getStatus() != TaskStatus.Completed || task.getStatus() != TaskStatus.Cancelled)
            Controller.getInstance().setOverdue(task);
    }
}
