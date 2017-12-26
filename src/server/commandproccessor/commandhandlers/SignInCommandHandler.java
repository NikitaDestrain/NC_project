package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.commandproccessor.User;
import server.controller.Controller;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;

public class SignInCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerCommandSender commandSender = ServerCommandSender.getInstance();
        Controller controller = Controller.getInstance();
        if (controller.isUserDataCorrect(((User) command.getObject()))) {
            commandSender.sendSuccessfulAuthCommand(ServerNetworkFacade.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));
            for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientDataOutputStreams())
                commandSender.sendUpdateCommand(controller.getJournal(), out);
        }
        else
            commandSender.sendUnsuccessfulAuthCommand(ServerNetworkFacade.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));

    }
}
