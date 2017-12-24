package server.commandproccessor;

import server.controller.Controller;
import server.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import java.io.*;

public class ParserCommand {

    public static Command parseToCommand (InputStream in) {
        Command command;
        try {
            System.out.println("Start reading command");
            JAXBContext context = JAXBContext.newInstance(Command.class);
            System.out.println("1");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.out.println("2");




            byte[] mData;
            try {

                       mData = new byte[in.available()];
                       in.read(mData);

                       InputStream is = new ByteArrayInputStream(mData);
                       command = (Command) unmarshaller.unmarshal(is);

                       return command;

                } catch(IOException e){
                    e.printStackTrace();
                }





            System.out.println("вызывается unmarshal");

            System.out.println("Command reading success");
        } catch (JAXBException e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println("Parse error!");
            return null;
        }
        return null;
       // return command;
    }

    public static void doCommandAction(Command command) {
        if (command != null) {
            switch (command.getName()) {
                case "Add" :
                    doAddAction((Task) command.getObject());
                    break;
                case "Edit" :
                    doEditAction((Task) command.getObject());
                    break;
                case "Delete" :
                    doDeleteAction((Task) command.getObject());
                    break;
                case "Later" :
                    doLaterAction((Task) command.getObject());
                    break;
                case "Finish" :
                    doFinishAction((Task) command.getObject());
                    break;
                case "Cancel" :
                    doCancelAction((Task) command.getObject());
                    break;
                case "Sign in" :
                    doSignInAction((User) command.getObject());
                    break;
                case "Sign up" :
                    doSignUpAction((User) command.getObject());
                    break;
            }
        }
    }

    private static synchronized void doAddAction(Task task) {
        Controller.getInstance().addTask(task);
    }

    private static synchronized void doEditAction(Task task) {
        Controller.getInstance().editTask(task);
    }

    private static synchronized void doDeleteAction(Task task) {
        Controller.getInstance().removeTask(task.getId());
    }

    private static synchronized void doLaterAction(Task task) {
        Controller.getInstance().updateNotification(task.getId());
    }

    private static synchronized void doFinishAction(Task task) {
        Controller.getInstance().finishNotification(task.getId());
    }

    private static synchronized void doCancelAction(Task task) {
        Controller.getInstance().cancelNotification(task.getId());
    }

    private static synchronized void doSignInAction(User user) {
        OutputStream out = null;
        if (Controller.getInstance().isUserDataCorrect(user)) { // верные логин и пароль
            ServerCommandProcessor.sendSuccessfulAuthCommand(out); // тут будет нужный поток вывода
        }
        else ServerCommandProcessor.sendUnSuccessfulAuthCommand(out);
    }

    private static synchronized void doSignUpAction(User user) {
        Controller controller = Controller.getInstance();
        OutputStream out = null;
        if (controller.isSuchLoginExists(user.getLogin())) // пользователь с таким логином существует
            ServerCommandProcessor.sendUnSuccessfulAuthCommand(out); // тут будет нужный поток вывода
        else {
            controller.addUser(user);
            ServerCommandProcessor.sendSuccessfulAuthCommand(out);
        }
    }
}
