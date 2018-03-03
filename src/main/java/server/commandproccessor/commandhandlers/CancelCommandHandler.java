package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.controller.Controller;
import server.model.Task;

public class CancelCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.cancelNotification(((Task) command.getObject()).getId());
    }
}
