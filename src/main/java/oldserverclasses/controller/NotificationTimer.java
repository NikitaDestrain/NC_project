package oldserverclasses.controller;

import auxiliaryclasses.MessageBox;
import oldserverclasses.commandproccessor.ServerCommandSender;
import server.exceptions.UnsuccessfulCommandActionException;
import server.model.Task;
import oldserverclasses.network.StreamContainer;

import java.io.DataOutputStream;
import java.util.List;
import java.util.TimerTask;

public class NotificationTimer extends TimerTask {
    private Task task;
    private List<DataOutputStream> clients;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();

    protected NotificationTimer(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        boolean overdue = true;
        clients = StreamContainer.getInstance().getClientNotificationOutputStreams();
        if (clients != null) {
            for (DataOutputStream client : clients) {
                System.out.println("Send notification to oldclientclasses.client");
                try {
                    commandSender.sendNotificationCommand(task, client);
                } catch (UnsuccessfulCommandActionException e) {
                    MessageBox.getInstance().showMessage("Could not send notification command!");
                }
                System.out.println("Success");
                overdue = false;
            }
        }
        if (overdue) {
            Controller.getInstance().setOverdue(task.getId());
        }
    }
}
