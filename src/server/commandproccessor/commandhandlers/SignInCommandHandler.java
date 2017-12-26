package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandSender;
import server.commandproccessor.User;
import server.controller.Controller;
import server.network.ServerNetworkFacade;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class SignInCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerCommandSender commandSender = ServerCommandSender.getInstance();
        Controller controller = Controller.getInstance();
        if (controller.isUserDataCorrect(((User) command.getObject()))) { // верные логин и пароль
            for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientDataOutputStreams()) {
                commandSender.sendSuccessfulAuthCommand(out); // тут будет нужный поток вывода
                commandSender.sendUpdateCommand(controller.getJournal(), out);
            }
        }
        else
            for (DataOutputStream out: ServerNetworkFacade.getInstance().getClientDataOutputStreams()) {
                commandSender.sendUnsuccessfulAuthCommand(out); // todo output stream everywhere
            }
    }
}
