package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.controller.Controller;
import server.model.Task;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;

public class FinishCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.finishNotification(((Task) command.getObject()).getId());
        for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientNotificationOutputStreams())
            ServerCommandSender.getInstance().sendUpdateCommand(controller.getJournal(), out);
    }
}
