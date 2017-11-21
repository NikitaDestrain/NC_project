package controller;

import GUI.NotificationWindow.NotificationForm;
import model.Task;
import model.TaskStatus;

import java.util.Date;
import java.util.List;
import java.util.Timer;

public class Notifier {

    private Task test_task = new Task("Test", TaskStatus.Planned, "Description",
            new Date(), new Date()); // тестовый

    //в конструктор будет приходить задача
    public Notifier() {
        addNotification();
    }

    private void addNotification(){ //todo private? parameters?
       NotificationTimer notificationTimer = new NotificationTimer(test_task);
       Timer timer = new Timer(true);
       timer.schedule(notificationTimer, test_task.getNotificationDate().getTime() - System.currentTimeMillis() + 5000);
       //todo Case: user change notification time. What will happen to the old timer?
    }
}
