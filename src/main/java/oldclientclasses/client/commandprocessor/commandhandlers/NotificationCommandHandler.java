package oldclientclasses.client.commandprocessor.commandhandlers;

import oldclientclasses.client.commandprocessor.Command;
import oldclientclasses.client.gui.notificationwindow.NotificationForm;
import oldclientclasses.client.gui.notificationwindow.Sound;
import server.model.Task;
import oldclientclasses.client.properties.ParserProperties;
import auxiliaryclasses.ConstantsClass;

import javax.swing.*;
import java.io.IOException;

public class NotificationCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        try {
            Sound.playSound(ParserProperties.getInstance().getProperties(ConstantsClass.NOTIF_SOUND));
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupted or missing!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        /*NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTask((Task) command.getObject());
        notificationForm.setVisible(true);*/
    }
}
