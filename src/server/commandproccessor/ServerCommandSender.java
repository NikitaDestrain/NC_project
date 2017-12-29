package server.commandproccessor;

import constants.ConstantsClass;
import server.factories.ServerCommandFactory;
import server.model.Journal;
import server.model.Task;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

public class ServerCommandSender {
    private static ServerCommandSender instance;

    private ServerCommandSender() {
    }

    public static ServerCommandSender getInstance() {
        if (instance == null) instance = new ServerCommandSender();
        return instance;
    }

    public void sendUpdateCommand(Journal journal, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UPDATE, journal), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void sendNotificationCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.NOTIFICATION, task), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendSuccessfulAuthCommand(Journal journal, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.SUCCESSFUL_AUTH, journal), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendUnsuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UNSUCCESSFUL_SIGN_IN, new User()), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendUnsuccessfulSignUpCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UNSUCCESSFUL_SIGN_UP, new User()),
                    out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendUnsuccessfulActionCommand(String string, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UNSUCCESSFUL_ACTION, string), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
