package oldserverclasses.commandproccessor.commandhandlers;

import oldserverclasses.commandproccessor.Command;
import oldserverclasses.controller.Controller;
import server.model.Task;

public class FinishCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.finishNotification(((Task) command.getObject()).getId());
    }
}
