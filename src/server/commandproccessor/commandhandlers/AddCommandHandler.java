package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.CommandSender;
import server.controller.Controller;
import server.factories.TaskFactory;
import server.model.Task;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class AddCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        Task tmp_task = (Task) command.getObject();
        controller.addTask(TaskFactory.createTask(tmp_task.getName(), tmp_task.getStatus(), tmp_task.getDescription(),
                tmp_task.getNotificationDate(), tmp_task.getPlannedDate()));
        for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientNotificationOutputStreams())
            CommandSender.getInstance().sendUpdateCommand(controller.getJournal(), out);
    }
}
