package server.commandproccessor;

import server.factories.ServerCommandFactory;
import server.model.Journal;
import server.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

public class ServerCommandSender {
    private static ServerCommandSender instance;
    private static final String UPDATE = "Update";
    private static final String NOTIFICATION = "Notification";
    private static final String UNSUCCESSFUL_SIGN_IN = "Unsuccessful auth";
    private static final String UNSUCCESSFUL_SIGN_UP = "Unsuccessful sign up";
    private static final String SUCCESSFUL_AUTH = "Successful auth";
    private static final String UNSUCCESSFUL_ACTION = "Unsuccessfully";

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
            /*marshaller.marshal(ServerCommandFactory.createCommand(UPDATE, journal), new File("test_update.xml"));
            byte[] buffer = new byte[(int) new File("test_update.xml").length()];
            FileInputStream inF = new FileInputStream("test_update.xml");
            inF.read(buffer);
            out.write(buffer);*/
            marshaller.marshal(ServerCommandFactory.createCommand(UPDATE, journal), out);
            out.flush();
        } catch (JAXBException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendNotificationCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(NOTIFICATION, task), out);
            out.flush();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSuccessfulAuthCommand(Journal journal, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(SUCCESSFUL_AUTH, journal), out);
            out.flush();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(UNSUCCESSFUL_SIGN_IN, new User()), out);
            out.flush();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsuccessfulSignUpCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(UNSUCCESSFUL_SIGN_UP, new User()),
                    out);
            out.flush();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsuccessfulActionCommand(String string, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(UNSUCCESSFUL_ACTION, string), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            e.getMessage();
        }
    }
}
