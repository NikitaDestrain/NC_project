package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.CommandSender;
import server.commandproccessor.User;
import server.controller.Controller;

import java.io.OutputStream;

public class SignInCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        OutputStream out = null;
        CommandSender commandSender = CommandSender.getInstance();
        Controller controller = Controller.getInstance();
        if (controller.isUserDataCorrect(((User) command.getObject()))) { // верные логин и пароль
            commandSender.sendSuccessfulAuthCommand(out); // тут будет нужный поток вывода
            commandSender.sendUpdateCommand(controller.getJournal(), out);
        }
        else commandSender.sendUnsuccessfulAuthCommand(out); // todo output stream everywhere
    }
}
