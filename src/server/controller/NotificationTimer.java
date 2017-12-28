package server.controller;

import server.commandproccessor.ServerCommandSender;
import server.gui.notificationwindow.Sound;
import server.model.Task;
import server.network.ServerNetworkFacade;
import server.properties.ParserProperties;
import server.properties.PropertiesConstant;

import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private Task task;
    private LinkedList<DataOutputStream> clients;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        boolean overdue = true;
        clients = ServerNetworkFacade.getInstance().getClientNotificationOutputStreams();
        if(clients != null) {
            for (DataOutputStream client : clients) {
                System.out.println("Send notification to client");
                commandSender.sendNotificationCommand(task, client);
                System.out.println("Success");
                overdue = false;
            }
        }
        if(overdue) {
            Controller.getInstance().setOverdue(task.getId());
        }
        Sound.playSound(ParserProperties.getInstance().getProperties(PropertiesConstant.NOTIF_SOUND.toString()));
    }
}
