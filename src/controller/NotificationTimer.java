package controller;

import gui.notificationwindow.NotificationForm;
import gui.notificationwindow.Sound;
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
        notificationForm = new NotificationForm();
        task.setStatus(TaskStatus.Completed);//todo статус должен менться по кнопке! Нотификация - это еще не команда к завершению таски, жизненным цмклом управляет пользователь через кнопки.
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
        Sound.playSound("sounds/snd.wav").join();//todo еще одно захардкоженное знаение. Напрашивается новая пропертя.
    }
}
