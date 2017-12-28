package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.notificationwindow.NotificationForm;
import client.gui.notificationwindow.Sound;
import client.model.Task;
import client.properties.ParserProperties;
import client.properties.PropertiesConstant;

public class NotificationCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Sound.playSound(ParserProperties.getInstance().getProperties(PropertiesConstant.NOTIF_SOUND.toString()));
        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTask((Task) command.getObject());
        notificationForm.setVisible(true);
    }
}
