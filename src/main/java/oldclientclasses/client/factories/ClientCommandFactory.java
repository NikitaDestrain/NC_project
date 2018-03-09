package oldclientclasses.client.factories;

import oldclientclasses.client.commandprocessor.Command;

public class ClientCommandFactory {

    public static Command createCommand(String name, Object object) {
        return new Command(name, object);
    }
}
