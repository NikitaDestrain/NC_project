package controller;

import gui.notificationwindow.NotificationForm;
import gui.notificationwindow.Sound;
import model.Task;
import properties.ParserProperties;

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
       // Sound.playSound("sounds/snd.wav").join();//todo еще одно захардкоженное знаение. Напрашивается новая пропертя.
        Sound.playSound(ParserProperties.getInstance().getProperties("NOTIF_SOUND"));
    }
}
