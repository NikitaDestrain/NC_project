package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.controller.Controller;
import server.model.Task;

public class LaterCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.updateNotification((Task) command.getObject());
    }
}
