package oldserverclasses.commandproccessor.commandhandlers;

import auxiliaryclasses.MessageBox;
import oldserverclasses.commandproccessor.Command;
import oldserverclasses.commandproccessor.ServerCommandSender;
import oldserverclasses.commandproccessor.User;
import oldserverclasses.controller.Controller;
import oldserverclasses.controller.UserAuthorizer;
import server.exceptions.UnsuccessfulCommandActionException;
import oldserverclasses.network.StreamContainer;

public class SignUpCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        UserAuthorizer authorizer = UserAuthorizer.getInstance();
        User user = ((User) command.getObject());
        ServerCommandSender commandSender = ServerCommandSender.getInstance();
        if (authorizer.isSuchLoginExists(user.getLogin()))
            try {
                commandSender.sendUnsuccessfulSignUpCommand(StreamContainer.getInstance().
                        getDataOutputStream(((User) command.getObject()).getPort()));
            } catch (UnsuccessfulCommandActionException e) {
                MessageBox.getInstance().showMessage("Could not send unsuccessful sign up command!");
            }
        else {
            authorizer.addUser(user.getLogin(), user.getPassword());
            try {
                commandSender.sendSuccessfulAuthCommand(controller.getJournal(),
                        StreamContainer.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));
            } catch (UnsuccessfulCommandActionException e) {
                MessageBox.getInstance().showMessage("Could not send successful auth command!");
            }
        }
    }
}
