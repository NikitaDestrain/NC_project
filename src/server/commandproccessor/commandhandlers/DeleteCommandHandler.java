package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.controller.Controller;
import server.model.Task;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;
import java.util.List;

public class DeleteCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.removeTask(((String) command.getObject()));
    }
}
