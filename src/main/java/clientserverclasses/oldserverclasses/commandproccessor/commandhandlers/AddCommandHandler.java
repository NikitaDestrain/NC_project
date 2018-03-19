package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.controller.Controller;
import server.factories.TaskFactory;
import server.model.Task;

public class AddCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        Task tmp_task = (Task) command.getObject();
        controller.addTask(TaskFactory.createTask(tmp_task));
    }
}
