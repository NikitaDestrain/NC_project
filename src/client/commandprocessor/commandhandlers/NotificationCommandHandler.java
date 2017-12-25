package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.notificationwindow.NotificationForm;
import client.model.Task;

public class NotificationCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        new NotificationForm().setTask((Task) command.getObject());
    }
}
