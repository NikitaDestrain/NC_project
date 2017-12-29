package client.commandprocessor;

import client.factories.ClientCommandFactory;
import client.model.Task;
import client.network.ClientNetworkFacade;
import constants.ConstantsClass;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ClientCommandSender {
    private static ClientCommandSender instance;
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
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.ADD, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendEditCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.EDIT, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendDeleteCommand(String tasksNums, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.DELETE, tasksNums), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendLaterCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.LATER, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendFinishCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.FINISH, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendCancelCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.CANCEL, task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendSignInCommand(String login, String password, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.SIGN_IN,
                    new User(login, password, clientNetworkFacade.getNotificationPort())), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void sendSignUpCommand(String login, String password, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.SIGN_UP,
                    new User(login, password, clientNetworkFacade.getNotificationPort())), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendDisconnectCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.DISCONNECT,
                    clientNetworkFacade.getNotificationPort()), out);
            out.flush();
        }
        catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
