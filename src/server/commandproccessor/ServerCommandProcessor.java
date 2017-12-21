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
    public static Command createUpdateCommand(Journal journal) {
        return ServerCommandFactory.createCommand("Update", journal);
    }

    //нотификация клиенту
    public static Command createNotificationCommand(Task task) {
        return ServerCommandFactory.createCommand("Notification", task);
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
}
