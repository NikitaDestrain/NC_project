package server.controller;

import server.model.Task;

import java.util.*;

public class Notifier {

    private Map<Integer, Timer> timers;

    protected Notifier() {
        timers = new HashMap<Integer, Timer>();
    }

    //выставление таймера для таски
    protected void addNotification(Task task) {
        NotificationTimer notificationTimer = new NotificationTimer(task);
        Timer timer = new Timer(true);
        timer.schedule(notificationTimer, task.getNotificationDate().getTime() - System.currentTimeMillis());
        timers.put(task.getId(),timer);
    }

    //отменяет таймер для таски
    protected void cancelNotification(int id){
        if(timers.containsKey(id)) {
            Timer timer = timers.get(id);
            timer.cancel();
        }
    }

    //приходит уже измененный таск с перенесенным временем
    protected void editNotification(Task task) {
        if(timers.containsKey(task.getId())) {
            Timer timer_old = timers.get(task.getId());
            timer_old.cancel();
            NotificationTimer notificationTimer = new NotificationTimer(task);
            Timer timer = new Timer(true);
            timer.schedule(notificationTimer, task.getNotificationDate().getTime() - System.currentTimeMillis());
            timers.put(task.getId(), timer);
        }
    }
}
