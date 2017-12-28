package client.commandprocessor;

import client.commandprocessor.commandhandlers.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ClientCommandParser {
    private static final String UPDATE = "Update";
    private static final String NOTIFICATION = "Notification";
    private static final String UNSUCCESSFUL_SIGN_IN = "Unsuccessful auth";
    private static final String UNSUCCESSFUL_SIGN_UP = "Unsuccessful sign up";
    private static final String SUCCESSFUL_AUTH = "Successful auth";
    private static final String UNSUCCESSFUL_ACTION = "Unsuccessfully";
    private static ClientCommandParser instance;
    private Map<String, CommandHandler> handlers;

    private ClientCommandParser() {
        handlers = new HashMap<>();
        handlers.put(UPDATE, new UpdateCommandHandler());
        handlers.put(NOTIFICATION, new NotificationCommandHandler());
        handlers.put(UNSUCCESSFUL_SIGN_IN, new UnsuccessfulSignInCommandHandler());
        handlers.put(UNSUCCESSFUL_SIGN_UP, new UnsuccessfulSignUpCommandHandler());
        handlers.put(SUCCESSFUL_AUTH, new SuccessfulAuthCommandHandler());
        handlers.put(UNSUCCESSFUL_ACTION, new UnsuccessfulActionCommandHandler());
    }

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
            System.out.println("Parse error!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doCommandAction(Command command) {
        if (command != null && handlers.containsKey(command.getName())) {
            handlers.get(command.getName()).handle(command);
        }
    }
}
