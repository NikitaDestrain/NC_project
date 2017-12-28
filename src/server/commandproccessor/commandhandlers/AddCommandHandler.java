package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.controller.Controller;
import server.factories.TaskFactory;
import server.model.Task;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;

public class AddCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        Task tmp_task = (Task) command.getObject();
        controller.addTask(TaskFactory.createTask(tmp_task.getName(), tmp_task.getStatus(), tmp_task.getDescription(),
                tmp_task.getNotificationDate(), tmp_task.getPlannedDate()));//todo напрашивается новый метод в класс фабрики, который будет создавать новую таску, как клон пришедшей (только с новым ID).
        for (DataOutputStream out : ServerNetworkFacade.getInstance().getClientNotificationOutputStreams()) {//todo vlla этот кусок кода без малейший отличий встречается в программе с десяток раз. Может стоит его вынести куда нибудь отдельно?
            ServerCommandSender.getInstance().sendUpdateCommand(controller.getJournal(), out);
        }
    }
}
