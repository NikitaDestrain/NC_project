package server.controller;

import server.commandproccessor.ServerCommandSender;
import server.gui.notificationwindow.NotificationForm;
import server.gui.notificationwindow.Sound;
import server.model.Task;
import server.network.ServerNetworkFacade;
import server.properties.ParserProperties;

import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private NotificationForm notificationForm;
    private Task task;
    private LinkedList<DataOutputStream> clients;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        clients = ServerNetworkFacade.getInstance().getClientNotificationOutputStreams();
        System.out.println(clients);
        for (DataOutputStream client: clients) {
            System.out.println("Send notification to client");
            commandSender.sendNotificationCommand(task, client);
            System.out.println("Success");
        }
        notificationForm = new NotificationForm();
        notificationForm.setTask(task);
        notificationForm.setVisible(true);
        Sound.playSound(ParserProperties.getInstance().getProperties("NOTIF_SOUND"));
    }
}
