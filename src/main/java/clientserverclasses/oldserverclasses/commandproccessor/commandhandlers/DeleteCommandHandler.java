package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.controller.Controller;

public class DeleteCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.removeTask(((String) command.getObject()));
    }
}
