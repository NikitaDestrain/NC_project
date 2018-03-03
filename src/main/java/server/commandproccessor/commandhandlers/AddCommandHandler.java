package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.controller.Controller;
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
