package client.commandprocessor;

import client.factories.ClientCommandFactory;
import client.model.Task;
import client.network.ClientNetworkFacade;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;

public class ClientCommandSender {
    private static ClientCommandSender instance;

    private ClientCommandSender() {}

    public static ClientCommandSender getInstance() {
        if (instance == null) instance = new ClientCommandSender();
        return instance;
    }

    public void sendAddCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand("Add", task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.printStackTrace();
            e.getMessage();
        }
    }

    public void sendEditCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand("Edit", task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.printStackTrace();
        }
    }

    public void sendDeleteCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand("Delete", task), out);
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
            marshaller.marshal(ClientCommandFactory.createCommand("Later", task), out);
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
            marshaller.marshal(ClientCommandFactory.createCommand("Finish", task), out);
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
            marshaller.marshal(ClientCommandFactory.createCommand("Cancel", task), out);
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
            marshaller.marshal(ClientCommandFactory.createCommand("Sign in", new User(login, password, ClientNetworkFacade.getInstance().getNotificationPort())), out);
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
            marshaller.marshal(ClientCommandFactory.createCommand("Sign up", new User(login, password, ClientNetworkFacade.getInstance().getNotificationPort())), out);
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
            marshaller.marshal(ClientCommandFactory.createCommand("Disconnect", ClientNetworkFacade.getInstance().getNotificationPort()), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            e.getMessage();
        }
    }
}
