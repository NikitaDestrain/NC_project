package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.controller.Controller;
import server.model.Task;

public class FinishCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.finishNotification(((Task) command.getObject()).getId());
    }
}
