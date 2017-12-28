package client.commandprocessor;

import client.factories.ClientCommandFactory;
import client.model.Task;
import client.network.ClientNetworkFacade;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ClientCommandSender {
    private static ClientCommandSender instance;
    private static final String ADD = "Add";
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";
    private static final String LATER = "Later";
    private static final String FINISH = "Finish";
    private static final String CANCEL = "Cancel";
    private static final String SIGN_IN = "Sign in";
    private static final String SIGN_UP = "Sign up";
    private static final String DISCONNECT = "Disconnect";
    private ClientNetworkFacade clientNetworkFacade;

    private ClientCommandSender() {
        clientNetworkFacade = ClientNetworkFacade.getInstance();
    }

    public static ClientCommandSender getInstance() {
        if (instance == null) instance = new ClientCommandSender();
        return instance;
    }

    public void sendAddCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ADD, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public void sendEditCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(EDIT, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.printStackTrace();
        }
    }

    public void sendDeleteCommand(String tasksNums, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(DELETE, tasksNums), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public void sendLaterCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(LATER, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public void sendFinishCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(FINISH, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public void sendCancelCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(CANCEL, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public void sendSignInCommand(String login, String password, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(SIGN_IN, new User(login, password, clientNetworkFacade.getNotificationPort())), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            e.getMessage();
        }

    }

    public void sendSignUpCommand(String login, String password, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(SIGN_UP, new User(login, password, clientNetworkFacade.getNotificationPort())), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            e.getMessage();
        }
    }

    public void sendDisconnectCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(DISCONNECT, clientNetworkFacade.getNotificationPort()), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            e.getMessage();
        }
    }
}
