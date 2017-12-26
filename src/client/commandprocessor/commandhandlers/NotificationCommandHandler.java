package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.notificationwindow.NotificationForm;
import client.model.Task;

public class NotificationCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTask((Task) command.getObject());
        notificationForm.setVisible(true);
    }
}
