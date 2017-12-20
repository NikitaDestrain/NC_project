package server.factories;

import commandProcessor.Command;

public class CommandFactory {

    public static Command createCommand(String name, Object object){
        return new Command(name, object);
    }
}
