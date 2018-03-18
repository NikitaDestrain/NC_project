package oldserverclasses.commandproccessor;

import auxiliaryclasses.ConstantsClass;
import oldserverclasses.exceptions.UnsuccessfulCommandActionException;
import server.factories.ServerCommandFactory;
import server.model.Journal;
import server.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

/**
 * Sends a command to the oldclientclasses.client side
 */

public class ServerCommandSender {
    private static ServerCommandSender instance;

    private ServerCommandSender() {
    }

    public static synchronized ServerCommandSender getInstance() {
        if (instance == null) instance = new ServerCommandSender();
        return instance;
    }

    private synchronized void sendCommand(Object object, String commandName, OutputStream out) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Command.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(ServerCommandFactory.createCommand(commandName, object), out);
        out.flush();
    }

    /**
     * Sends an update command with a journal from the controller
     *
     * @param journal
     * @param out     data stream on which command will be transferred
     */

    public void sendUpdateCommand(Journal journal, OutputStream out) {
        try {
            sendCommand(journal, ConstantsClass.UPDATE, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a notification command with task which notification time has come
     *
     * @param task
     * @param out  data stream on which command will be transferred
     */

    public void sendNotificationCommand(Task task, OutputStream out) {
        try {
            sendCommand(task, ConstantsClass.NOTIFICATION, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of a successful authorization of a user with a journal from the controller
     *
     * @param journal
     * @param out     data stream on which command will be transferred
     */

    public void sendSuccessfulAuthCommand(Journal journal, OutputStream out) {
        try {
            sendCommand(journal, ConstantsClass.SUCCESSFUL_AUTH, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of an unsuccessful authorization of a user
     *
     * @param out data stream on which command will be transferred
     */

    public void sendUnsuccessfulAuthCommand(OutputStream out) {
        try {
            sendCommand(new User(), ConstantsClass.UNSUCCESSFUL_SIGN_IN, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of an unsuccessful sign up of a user
     *
     * @param out data stream on which command will be transferred
     */

    public void sendUnsuccessfulSignUpCommand(OutputStream out) {
        try {
            sendCommand(new User(), ConstantsClass.UNSUCCESSFUL_SIGN_UP, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }

    /**
     * Sends a command of an unsuccessful action requested by user
     *
     * @param out data stream on which command will be transferred
     */

    public void sendUnsuccessfulActionCommand(String string, OutputStream out) {
        try {
            sendCommand(string, ConstantsClass.UNSUCCESSFUL_ACTION, out);
        } catch (JAXBException | IOException e) {
            throw new UnsuccessfulCommandActionException();
        }
    }
}
