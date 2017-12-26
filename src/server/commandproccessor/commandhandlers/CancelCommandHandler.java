package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.CommandSender;
import server.controller.Controller;
import server.model.Task;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class CancelCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.cancelNotification(((Task) command.getObject()).getId());
        for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientNotificationOutputStreams())
            CommandSender.getInstance().sendUpdateCommand(controller.getJournal(), out);
    }
}
