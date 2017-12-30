package server.commandproccessor;

import constants.ConstantsClass;
import server.commandproccessor.commandhandlers.*;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses an incoming command on the server side
 */

public class ServerCommandParser {
    private static ServerCommandParser instance;
    private Map<String, CommandHandler> handlers;

    private ServerCommandParser() {
        handlers = new HashMap<>();
        handlers.put(ConstantsClass.ADD, new AddCommandHandler());
        handlers.put(ConstantsClass.EDIT, new EditCommandHandler());
        handlers.put(ConstantsClass.DELETE, new DeleteCommandHandler());
        handlers.put(ConstantsClass.LATER, new LaterCommandHandler());
        handlers.put(ConstantsClass.FINISH, new FinishCommandHandler());
        handlers.put(ConstantsClass.CANCEL, new CancelCommandHandler());
        handlers.put(ConstantsClass.SIGN_IN, new SignInCommandHandler());
        handlers.put(ConstantsClass.SIGN_UP, new SignUpCommandHandler());
        handlers.put(ConstantsClass.DISCONNECT, new DisconnectCommandHandler());
    }

    public static ServerCommandParser getInstance() { //todo vlla synchronized
        if (instance == null) instance = new ServerCommandParser();
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
            JOptionPane.showMessageDialog(null, "Incorrect command!",
                    "Parse error!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * Performs an execution of an incoming command using its name
     * @param command to be executed
     */

    public int doCommandAction(Command command) {
        if (command != null)
            if (handlers.containsKey(command.getName()))
                handlers.get(command.getName()).handle(command);
            else return 1;
        return 0;
        //todo vlla если команда выполнена без проблем или команда = null - возвращаем 0
        //если команды нет среди зарегистрированных - возвращаем 1
        //очень странная логика
        //и если эта логика соответсвует вашей задумке - ок, но тогда хоть опишите ее в джавадоке
        //потому что, например, для меня - она совершенно не очевидна
    }
}
