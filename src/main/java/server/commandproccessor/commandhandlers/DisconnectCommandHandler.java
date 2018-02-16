package server.commandproccessor.commandhandlers;

import server.network.ServerProcessor;
import server.commandproccessor.Command;

public class DisconnectCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerProcessor.getInstance().finishClient((Integer) command.getObject());
    }
}
