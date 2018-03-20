package clientserverclasses.oldclientclasses.client.factories;

import clientserverclasses.oldclientclasses.client.commandprocessor.Command;

public class ClientCommandFactory {

    public static Command createCommand(String name, Object object) {
        return new Command(name, object);
    }
}
