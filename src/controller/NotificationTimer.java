package controller;

import GUI.NotificationWindow.NotificationForm;
import model.Task;

import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private NotificationForm notificationForm;
    private Task task;

    public NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        setTask();
    }

    private void setTask() {
        notificationForm = new NotificationForm();
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
    }
}
