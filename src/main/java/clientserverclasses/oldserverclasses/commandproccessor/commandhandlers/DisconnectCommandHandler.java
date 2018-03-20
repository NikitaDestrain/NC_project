package clientserverclasses.oldserverclasses.commandproccessor.commandhandlers;

import clientserverclasses.oldserverclasses.network.ServerProcessor;
import clientserverclasses.oldserverclasses.commandproccessor.Command;

public class DisconnectCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerProcessor.getInstance().finishClient((Integer) command.getObject());
    }
}
