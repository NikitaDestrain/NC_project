package client.commandprocessor;

import auxiliaryclasses.MessageBox;
import client.commandprocessor.commandhandlers.*;
import auxiliaryclasses.ConstantsClass;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses an incoming command on the client side
 */

public class ClientCommandParser {
    private static ClientCommandParser instance;
    private Map<String, CommandHandler> handlers;
    private MessageBox messageBox = MessageBox.getInstance();

    private ClientCommandParser() {
        handlers = new HashMap<>();
        handlers.put(ConstantsClass.UPDATE, new UpdateCommandHandler());
        handlers.put(ConstantsClass.NOTIFICATION, new NotificationCommandHandler());
        handlers.put(ConstantsClass.UNSUCCESSFUL_SIGN_IN, new UnsuccessfulSignInCommandHandler());
        handlers.put(ConstantsClass.UNSUCCESSFUL_SIGN_UP, new UnsuccessfulSignUpCommandHandler());
        handlers.put(ConstantsClass.SUCCESSFUL_AUTH, new SuccessfulAuthCommandHandler());
        handlers.put(ConstantsClass.UNSUCCESSFUL_ACTION, new UnsuccessfulActionCommandHandler());
    }

    public static ClientCommandParser getInstance() {
        if (instance == null) instance = new ClientCommandParser();
        return instance;
    }

    /**
     * Parses an incoming command from byte array of XML context using Unmarshaller
     * @param dataArr incoming data array
     * @return Command object
     */

    public Command parseToCommand(byte[] dataArr) {
        Command command;
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = new ByteArrayInputStream(dataArr);
            command = (Command) unmarshaller.unmarshal(is);
            is.close();
            return command;
        } catch (JAXBException | IOException e) {
            messageBox.showMessage(ConstantsClass.INCORRECT_COMMAND);
        }
        return null;
    }

    /**
     * Performs an execution of an incoming command using its name
     * @param command to be executed
     */

    public void doCommandAction(Command command) {
        if (command != null && handlers.containsKey(command.getName())) {
            handlers.get(command.getName()).handle(command);
        }
    }
}
