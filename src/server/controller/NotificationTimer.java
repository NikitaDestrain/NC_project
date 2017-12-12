package server.controller;

import server.gui.notificationwindow.NotificationForm;
import server.gui.notificationwindow.Sound;
import server.model.Task;
import server.properties.ParserProperties;

import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private NotificationForm notificationForm;
    private Task task;

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        notificationForm = new NotificationForm();
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
        Sound.playSound(ParserProperties.getInstance().getProperties("NOTIF_SOUND"));
    }
}
