package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.MessageBox;
import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.commandproccessor.ServerCommandSender;
import clientserverclasses.oldserverclasses.commandproccessor.User;
import clientserverclasses.oldserverclasses.controller.Controller;
import clientserverclasses.oldserverclasses.controller.UserAuthorizer;
import clientserverclasses.oldserverclasses.exceptions.UnsuccessfulCommandActionException;
import clientserverclasses.oldserverclasses.network.StreamContainer;

public class SignInCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerCommandSender commandSender = ServerCommandSender.getInstance();
        UserAuthorizer authorizer = UserAuthorizer.getInstance();
        Controller controller = Controller.getInstance();
        if (authorizer.isUserDataCorrect(((User) command.getObject()).getLogin(), ((User) command.getObject()).getPassword())) {
            try {
                commandSender.sendSuccessfulAuthCommand(controller.getJournal(),
                        StreamContainer.getInstance().getDataOutputStream(((User) command.getObject()).getPort()));
            } catch (UnsuccessfulCommandActionException e) {
                MessageBox.getInstance().showMessage("Could not send successful auth command!");
            }
        } else
            try {
                commandSender.sendUnsuccessfulAuthCommand(StreamContainer.getInstance().
                        getDataOutputStream(((User) command.getObject()).getPort()));
            } catch (UnsuccessfulCommandActionException e) {
                MessageBox.getInstance().showMessage("Could not send unsuccessful auth command!");
            }
    }
}
