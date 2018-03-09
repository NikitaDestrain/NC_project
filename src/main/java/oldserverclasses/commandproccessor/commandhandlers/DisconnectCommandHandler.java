package oldserverclasses.commandproccessor.commandhandlers;

import oldserverclasses.network.ServerProcessor;
import oldserverclasses.commandproccessor.Command;

public class DisconnectCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ServerProcessor.getInstance().finishClient((Integer) command.getObject());
    }
}
