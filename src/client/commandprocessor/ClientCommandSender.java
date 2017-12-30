package client.commandprocessor;

import client.factories.ClientCommandFactory;
import client.model.Task;
import client.network.ClientNetworkFacade;
import constants.ConstantsClass;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Sends a command to the server side
 */

public class ClientCommandSender {
    //todo vlla все те же замечания, что и для ServerCommandSender
    private static ClientCommandSender instance;
    private ClientNetworkFacade clientNetworkFacade;

    private ClientCommandSender() {
        clientNetworkFacade = ClientNetworkFacade.getInstance();
    }

    public static ClientCommandSender getInstance() {
        if (instance == null) instance = new ClientCommandSender();
        return instance;
    }

    /**
     * Sends a command of adding a task
     * @param task to be added
     * @param out data stream on which command will be transferred
     */

    public void sendAddCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.ADD, task), out);
            out.flush();

        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of editing a task
     * @param task to be edited
     * @param out data stream on which command will be transferred
     */

    public void sendEditCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.EDIT, task), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of deleting a couple tasks
     * @param tasksNumbers task numbers in a string splitted by ','
     * @param out data stream on which command will be transferred
     */

    public void sendDeleteCommand(String tasksNumbers, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.DELETE, tasksNumbers), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of deferring a task
     * @param task to be deferred
     * @param out data stream on which command will be transferred
     */

    public void sendLaterCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.LATER, task), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of finishing a task
     * @param task to be finished
     * @param out data stream on which command will be transferred
     */

    public void sendFinishCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.FINISH, task), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of cancelling a task
     * @param task to be cancelled
     * @param out data stream on which command will be transferred
     */

    public void sendCancelCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.CANCEL, task), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of signing in
     * @param login of a user
     * @param password of a user
     * @param out data stream on which command will be transferred
     */

    public void sendSignInCommand(String login, String password, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.SIGN_IN,
                    new User(login, password, clientNetworkFacade.getNotificationPort())), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of signing up
     * @param login of a user
     * @param password of a user
     * @param out data stream on which command will be transferred
     */

    public void sendSignUpCommand(String login, String password, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.SIGN_UP,
                    new User(login, password, clientNetworkFacade.getNotificationPort())), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            if (JOptionPane.showConfirmDialog(null, "You should restart application! Connection with server was lost! Close the app?",
                    "Connection error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Sends a command of a disconnecting
     * @param out data stream on which command will be transferred
     */

    public void sendDisconnectCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ClientCommandFactory.createCommand(ConstantsClass.DISCONNECT,
                    clientNetworkFacade.getNotificationPort()), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Crush disconnect! Server is offline!",
                    "Connection error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
