package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.controller.Controller;

public class DeleteCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.removeTask(((String) command.getObject()));
    }
}
