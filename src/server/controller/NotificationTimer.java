package server.controller;

import server.commandproccessor.CommandSender;
import server.gui.notificationwindow.NotificationForm;
import server.gui.notificationwindow.Sound;
import server.model.Task;
import server.network.ServerNetworkFacade;
import server.properties.ParserProperties;

import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private NotificationForm notificationForm;
    private Task task;
    private LinkedList<DataOutputStream> clients;
    private CommandSender commandSender = CommandSender.getInstance();

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        clients = ServerNetworkFacade.getInstance().getClientNotificationOutputStreams();
        for (DataOutputStream client: clients)
            commandSender.sendNotificationCommand(task, client);
        notificationForm = new NotificationForm();
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
        Sound.playSound(ParserProperties.getInstance().getProperties("NOTIF_SOUND"));
    }
}
