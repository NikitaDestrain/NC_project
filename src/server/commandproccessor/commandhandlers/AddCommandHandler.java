package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.CommandSender;
import server.controller.Controller;
import server.model.Task;

import java.io.OutputStream;

public class AddCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        controller.addTask((Task) command.getObject());
        OutputStream out = null;
        CommandSender.getInstance().sendUpdateCommand(controller.getJournal(), out); // todo output stream
    }
}
