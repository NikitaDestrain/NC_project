package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.commandproccessor.User;
import server.controller.Controller;
import server.controller.UserAuthorizer;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class SignUpCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        UserAuthorizer authorizer = UserAuthorizer.getInstance();
        User user = ((User) command.getObject());
        ServerCommandSender commandSender = ServerCommandSender.getInstance();
        if (authorizer.isSuchLoginExists(user.getLogin()))
            commandSender.sendUnsuccessfulSignUpCommand(ServerNetworkFacade.getInstance().
                    getDataOutputStream(((User) command.getObject()).getPort()));
        else {
            authorizer.addUser(user);
            commandSender.sendSuccessfulAuthCommand(controller.getJournal(),
                    ServerNetworkFacade.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));
        }
    }
}
