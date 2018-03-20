package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.controller.Controller;
import server.model.Task;

public class CancelCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.cancelNotification(((Task) command.getObject()).getId());
    }
}
