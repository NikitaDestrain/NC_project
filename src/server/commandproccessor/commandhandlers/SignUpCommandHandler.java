package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.CommandSender;
import server.commandproccessor.User;
import server.controller.Controller;

import java.io.OutputStream;

public class SignUpCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        Controller controller = Controller.getInstance();
        User user = ((User) command.getObject());
        OutputStream out = null;
        CommandSender commandSender = CommandSender.getInstance();
        if (controller.isSuchLoginExists(user.getLogin())) // пользователь с таким логином существует
            commandSender.sendUnsuccessfulAuthCommand(out); // тут будет нужный поток вывода
        else {
            controller.addUser(user);
            commandSender.sendSuccessfulAuthCommand(out);
            commandSender.sendUpdateCommand(controller.getJournal(), out); // todo output stream everywhere
        }
    }
}
