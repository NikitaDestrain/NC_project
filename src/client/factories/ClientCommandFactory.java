package client.factories;

import client.commandprocessor.Command;
import client.commandprocessor.temporary.AuthCommand;

public class ClientCommandFactory {

    public static Command createCommand(String name, Object object){
        return new Command(name, object);
    }
    public static AuthCommand createAuthCommand(String name, String login, String password) {
        return new AuthCommand(name, login, password);
    }
}
