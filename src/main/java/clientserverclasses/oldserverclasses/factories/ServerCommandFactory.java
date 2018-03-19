package clientserverclasses.oldserverclasses.factories;

import clientserverclasses.oldserverclasses.commandproccessor.Command;

public class ServerCommandFactory {

    public static Command createCommand(String name, Object object) {
        return new Command(name, object);
    }
}
