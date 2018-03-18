package oldserverclasses.commandproccessor.commandhandlers;

import auxiliaryclasses.MessageBox;
import oldserverclasses.commandproccessor.Command;
import oldserverclasses.commandproccessor.ServerCommandSender;
import oldserverclasses.commandproccessor.User;
import oldserverclasses.controller.Controller;
import oldserverclasses.controller.UserAuthorizer;
import oldserverclasses.exceptions.UnsuccessfulCommandActionException;
import oldserverclasses.network.StreamContainer;

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
