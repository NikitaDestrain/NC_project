package clientserverclasses.oldserverclasses.commandproccessor;

import auxiliaryclasses.ConstantsClass;
import clientserverclasses.MessageBox;
import clientserverclasses.oldserverclasses.commandproccessor.commandhandlers.*;

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
    private MessageBox messageBox = MessageBox.getInstance();

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

    public static synchronized ServerCommandParser getInstance() {
        if (instance == null) instance = new ServerCommandParser();
        return instance;
    }

    /**
     * Parses an incoming command from byte array of XML context using Unmarshaller
     *
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
     *
     * @param command to be executed
     * @return 0 if the command is not null and the command's name is existing in the handlers map. 1 otherwise
     */

    public int doCommandAction(Command command) {
        if (command != null)
            if (handlers.containsKey(command.getName()))
                handlers.get(command.getName()).handle(command);
            else return 1;
        return 0;
    }
}
