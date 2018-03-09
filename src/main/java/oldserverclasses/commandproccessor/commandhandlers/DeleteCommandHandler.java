package oldserverclasses.commandproccessor.commandhandlers;

import oldserverclasses.commandproccessor.Command;
import oldserverclasses.controller.Controller;

public class DeleteCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.removeTask(((String) command.getObject()));
    }
}
