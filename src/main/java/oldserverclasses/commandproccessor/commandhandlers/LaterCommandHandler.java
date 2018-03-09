package oldserverclasses.commandproccessor.commandhandlers;

import oldserverclasses.commandproccessor.Command;
import oldserverclasses.controller.Controller;
import server.model.Task;

public class LaterCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.updateNotification((Task) command.getObject());
    }
}
