package client.commandprocessor;

import client.commandprocessor.commandhandlers.*;
import constants.ConstantsClass;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ClientCommandParser {
    private static ClientCommandParser instance;
    private Map<String, CommandHandler> handlers;

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
            JOptionPane.showMessageDialog(null, "Incorrect command!",
                    "Parse error!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void doCommandAction(Command command) {
        if (command != null && handlers.containsKey(command.getName())) {
            handlers.get(command.getName()).handle(command);
        }
    }
}
