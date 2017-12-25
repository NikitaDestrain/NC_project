package client.commandprocessor;


import client.commandprocessor.commandhandlers.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class CommandParser {
    private static final String UPDATE = "Update";
    private static final String NOTIFICATION = "Notification";
    private static final String UNSUCCESSFUL_AUTH = "Unsuccessful auth";
    private static final String UNSUCCESSFUL_SIGN_UP = "Unsuccessful sign up";
    private static final String SUCCESSFUL_AUTH = "Successful auth";
    private static final String UNSUCCESSFUL_ACTION = "Unsuccessfully";
    private static CommandParser instance;

    private CommandParser() {}

    public static CommandParser getInstance() {
        if (instance == null) instance = new CommandParser();
        return instance;
    }

    public Command parseToCommand (InputStream in) {
        Command command;
        try {
            System.out.println("Start reading command");
            JAXBContext context = JAXBContext.newInstance(server.commandproccessor.Command.class);
            System.out.println("1");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.out.println("2");
            command = (Command) unmarshaller.unmarshal(in);
            System.out.println("Command reading success");
        } catch (JAXBException e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println("Parse error!");
            return null;
        }
        return command;
    }

    public void doCommandAction(Command command) {
        if (command != null) {
            switch (command.getName()) {
                case UPDATE :
                    new UpdateCommandHandler().handle(command);
                    break;
                case NOTIFICATION :
                    new NotificationCommandHandler().handle(command);
                    break;
                case UNSUCCESSFUL_AUTH:
                    new UnsuccessfulSignInCommandHandler().handle(command);
                    break;
                case UNSUCCESSFUL_SIGN_UP :
                    new UnsuccessfulSignUpCommandHandler().handle(command);
                    break;
                case SUCCESSFUL_AUTH:
                    new SuccessfulAuthCommandHandler().handle(command);
                    break;
                case UNSUCCESSFUL_ACTION :
                    new UnsuccessfulActionCommandHandler().handle(command);
                    break;
            }
        }
    }
}
