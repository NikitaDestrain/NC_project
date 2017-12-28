package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;
import server.network.ServerNetworkFacade;

public class DisconnectCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerNetworkFacade.getInstance().finishClient((Integer) command.getObject());
    }
}
