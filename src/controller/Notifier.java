package controller;

import GUI.NotificationWindow.NotificationForm;
import model.Task;
import model.TaskStatus;

import java.util.Date;

public class Notifier {
    private NotificationForm notificationForm;

    private Task task = new Task("Test", TaskStatus.Planned, "Description",
            new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())); // тестовый

    public Notifier() {
    }

    public void setTask() {
        notificationForm = new NotificationForm();
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
    }


}
