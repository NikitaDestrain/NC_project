package server.commandproccessor;

import server.factories.ServerCommandFactory;
import server.model.Journal;
import server.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;

public class ServerCommandProcessor {

    //апдейт журнала
    private static Command createUpdateCommand(Journal journal) {
        return ServerCommandFactory.createCommand("Update", journal);
    }

    //нотификация клиенту
    private static Command createNotificationCommand(Task task) {
        return ServerCommandFactory.createCommand("Notification", task);
    }

    private static Command createSuccessfulAuthCommand() {
        return ServerCommandFactory.createCommand("Successful auth", null);
    }

    private static Command createUnSuccessfulAuthCommand() {
        return ServerCommandFactory.createCommand("Unsuccessful auth", null);
    }

    public static void sendUpdateCommand(Journal journal, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createUpdateCommand(journal), out);
        }
        catch(JAXBException e){
            e.printStackTrace();
        }
    }

    public static void sendNotificationCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createNotificationCommand(task), out);
        }
        catch(JAXBException e){
            e.printStackTrace();
        }
    }

    public static void sendSuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createSuccessfulAuthCommand(), out);
        }
        catch(JAXBException e){
            e.printStackTrace();
        }
    }

    public static void sendUnSuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createUnSuccessfulAuthCommand(), out);
        }
        catch(JAXBException e){
            e.printStackTrace();
        }
    }
}
