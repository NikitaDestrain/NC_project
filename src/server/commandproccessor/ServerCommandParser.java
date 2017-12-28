package server.commandproccessor;

import server.commandproccessor.commandhandlers.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServerCommandParser {
    private static final String ADD = "Add";
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";
    private static final String LATER = "Later";
    private static final String FINISH = "Finish";
    private static final String CANCEL = "Cancel";
    private static final String SIGN_IN = "Sign in";
    private static final String SIGN_UP = "Sign up";
    private static final String DISCONNECT = "Disconnect";
    private static ServerCommandParser instance;
    private Map<String, CommandHandler> handlers;

    private ServerCommandParser() {
        handlers = new HashMap<>();
        handlers.put(ADD, new AddCommandHandler());
        handlers.put(EDIT, new EditCommandHandler());
        handlers.put(DELETE, new DeleteCommandHandler());
        handlers.put(LATER, new LaterCommandHandler());
        handlers.put(FINISH, new FinishCommandHandler());
        handlers.put(CANCEL, new CancelCommandHandler());
        handlers.put(SIGN_IN, new SignInCommandHandler());
        handlers.put(SIGN_UP, new SignUpCommandHandler());
        handlers.put(DISCONNECT, new DisconnectCommandHandler());
    }

    public static ServerCommandParser getInstance() {
        if (instance == null) instance = new ServerCommandParser();
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

    public int doCommandAction(Command command) {
        if (command != null)
            if (handlers.containsKey(command.getName()))
                handlers.get(command.getName()).handle(command);
            else return 1;
        return 0;
    }
}
