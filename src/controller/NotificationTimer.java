package controller;

import gui.notificationwindow.NotificationForm;
import model.Task;
import model.TaskStatus;

import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private NotificationForm notificationForm;
    private Task task;

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        setTask();
    }

    private void setTask() {
        System.out.println("Hi! From Notif Timer!");
        notificationForm = new NotificationForm();
        task.setStatus(TaskStatus.Completed);
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
    }
}
