package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.exceptions.IllegalPropertyException;
import client.gui.notificationwindow.NotificationForm;
import client.gui.notificationwindow.Sound;
import client.model.Task;
import client.properties.ParserProperties;
import client.properties.PropertiesConstant;

import javax.swing.*;
import java.io.IOException;

public class NotificationCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        try {
            Sound.playSound(ParserProperties.getInstance().getProperties(PropertiesConstant.NOTIF_SOUND.toString()));
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupted or missing!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        NotificationForm notificationForm = new NotificationForm();
        notificationForm.setTask((Task) command.getObject());
        notificationForm.setVisible(true);
    }
}
