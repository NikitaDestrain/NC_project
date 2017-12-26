package server.commandproccessor;

import server.factories.ServerCommandFactory;
import server.model.Journal;
import server.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
            marshaller.marshal(ServerCommandFactory.createCommand("Update", journal), out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand("Notification", task), out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void sendSuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand("Successful auth", new User()), out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand("Unsuccessful auth", new User()), out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsuccessfulSignUpCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand("Unsuccessful sign up", new User()),
                    out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsuccessfulActionCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand("Unsuccessfully", new User()), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            e.getMessage();
        }
    }
}
