package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.MessageBox;
import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.commandproccessor.ServerCommandSender;
import clientserverclasses.oldserverclasses.commandproccessor.User;
import clientserverclasses.oldserverclasses.controller.Controller;
import clientserverclasses.oldserverclasses.controller.UserAuthorizer;
import clientserverclasses.oldserverclasses.exceptions.UnsuccessfulCommandActionException;
import clientserverclasses.oldserverclasses.network.StreamContainer;

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
