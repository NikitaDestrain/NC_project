package server.commandproccessor;

import server.commandproccessor.commandhandlers.*;
import server.controller.Controller;
import server.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class CommandParser {
    private static final String ADD = "Add";
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";
    private static final String LATER = "Later";
    private static final String FINISH = "Finish";
    private static final String CANCEL = "Cancel";
    private static final String SIGN_IN = "Sign in";
    private static final String SIGN_UP = "Sign up";
    private static final String DISCONNECT = "Disconnect";
    private static CommandParser instance;

    private CommandParser() {}

    public static CommandParser getInstance() {
        if (instance == null) instance = new CommandParser();
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

    public int doCommandAction(Command command) {
        if (command != null) {
            switch (command.getName()) {
                case ADD:
                    new AddCommandHandler().handle(command);
                    break;
                case EDIT:
                    new EditCommandHandler().handle(command);
                    break;
                case DELETE:
                    new DeleteCommandHandler().handle(command);
                    break;
                case LATER:
                    new LaterCommandHandler().handle(command);
                    break;
                case FINISH:
                    new FinishCommandHandler().handle(command);
                    break;
                case CANCEL:
                    new CancelCommandHandler().handle(command);
                    break;
                case SIGN_IN:
                    new SignInCommandHandler().handle(command);
                    break;
                case SIGN_UP:
                    new SignUpCommandHandler().handle(command);
                    break;
                case DISCONNECT:
                    return 1;
            }
        }
        return 0;
    }
}
