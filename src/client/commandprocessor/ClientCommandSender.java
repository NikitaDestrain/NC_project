package client.commandprocessor;

import client.factories.ClientCommandFactory;
import server.model.Task;
import client.network.ClientNetworkFacade;
import auxiliaryclasses.ConstantsClass;
import server.exceptions.UnsuccessfulCommandActionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Sends a command to the server side
 */

public class ClientCommandSender {
    //todo vlla все те же замечания, что и для ServerCommandSender DONE
    private static ClientCommandSender instance;
    private ClientNetworkFacade clientNetworkFacade;

    private ClientCommandSender() {
        clientNetworkFacade = ClientNetworkFacade.getInstance();
    }

    public synchronized static ClientCommandSender getInstance() {
        if (instance == null) instance = new ClientCommandSender();
        return instance;
    }

    private synchronized void sendCommand(Object object, String commandName, OutputStream out) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Command.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(ClientCommandFactory.createCommand(commandName, object), out);
        out.flush();
    }

    /**
     * Sends a command of adding a task
     * @param task to be added
     * @param out data stream on which command will be transferred
     */

    public void sendAddCommand(Task task, OutputStream out) {
        try {
            sendCommand(task, ConstantsClass.ADD, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of editing a task
     * @param task to be edited
     * @param out data stream on which command will be transferred
     */

    public void sendEditCommand(Task task, OutputStream out) {
        try {
            sendCommand(task, ConstantsClass.EDIT, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of deleting a couple tasks
     * @param tasksNumbers task numbers in a string splitted by ','
     * @param out data stream on which command will be transferred
     */

    public void sendDeleteCommand(String tasksNumbers, OutputStream out) {
        try {
            sendCommand(tasksNumbers, ConstantsClass.DELETE, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of deferring a task
     * @param task to be deferred
     * @param out data stream on which command will be transferred
     */

    public void sendLaterCommand(Task task, OutputStream out) {
        try {
            sendCommand(task, ConstantsClass.LATER, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of finishing a task
     * @param task to be finished
     * @param out data stream on which command will be transferred
     */

    public void sendFinishCommand(Task task, OutputStream out) {
        try {
            sendCommand(task, ConstantsClass.FINISH, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of cancelling a task
     * @param task to be cancelled
     * @param out data stream on which command will be transferred
     */

    public void sendCancelCommand(Task task, OutputStream out) {
        try {
            sendCommand(task, ConstantsClass.CANCEL, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
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
            sendCommand(new User(login, password, clientNetworkFacade.getNotificationPort()), ConstantsClass.SIGN_IN, out);
            out.flush();
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
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
            sendCommand(new User(login, password, clientNetworkFacade.getNotificationPort()), ConstantsClass.SIGN_UP, out);
            out.flush();
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of a disconnecting
     * @param out data stream on which command will be transferred
     */

    public void sendDisconnectCommand(OutputStream out) {
        try {
            sendCommand(clientNetworkFacade.getNotificationPort(), ConstantsClass.DISCONNECT, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }
}
