package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.commandproccessor.User;
import server.controller.Controller;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class SignUpCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        User user = ((User) command.getObject());
        ServerCommandSender commandSender = ServerCommandSender.getInstance();
        if (controller.isSuchLoginExists(user.getLogin()))
            commandSender.sendUnsuccessfulSignUpCommand(ServerNetworkFacade.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));
        else {
            controller.addUser(user);
            commandSender.sendSuccessfulAuthCommand(ServerNetworkFacade.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));
            for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientDataOutputStreams())
                commandSender.sendUpdateCommand(controller.getJournal(), out);
        }
    }
}
