package server.commandproccessor.commandhandlers;

import server.commandproccessor.Command;

public interface CommandHandler {
    void handle(Command command);
}
