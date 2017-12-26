package client.commandprocessor;

import client.commandprocessor.commandhandlers.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClientCommandParser {
    private static final String UPDATE = "Update";
    private static final String NOTIFICATION = "Notification";
    private static final String UNSUCCESSFUL_AUTH = "Unsuccessful auth";
    private static final String UNSUCCESSFUL_SIGN_UP = "Unsuccessful sign up";
    private static final String SUCCESSFUL_AUTH = "Successful auth";
    private static final String UNSUCCESSFUL_ACTION = "Unsuccessfully";
    private static ClientCommandParser instance;

    private ClientCommandParser() {}

    public static ClientCommandParser getInstance() {
        if (instance == null) instance = new ClientCommandParser();
        return instance;
    }

    public Command parseToCommand(byte[] dataArr) {
        Command command;
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = new ByteArrayInputStream(dataArr);
            command = (Command) unmarshaller.unmarshal(is);
            is.close();
            return command;
        } catch (JAXBException e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println("Parse error!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
