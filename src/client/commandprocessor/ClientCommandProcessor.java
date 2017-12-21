package client.commandprocessor;

import client.factories.ClientCommandFactory;
import client.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;

public class ClientCommandProcessor {
    //создание
    private static Command createAddCommand(Task task) { return ClientCommandFactory.createCommand("Add", task); }

    //изменение
    private static Command createEditCommand(Task task) {
        return ClientCommandFactory.createCommand("Edit", task);
    }

    //удаление
    private static Command createDeleteCommand(Task task) {
        return ClientCommandFactory.createCommand("Delete", task);
    }

    //отложить нотификацию(идут серверу)
    private static Command createLaterCommand(Task task) {
        return ClientCommandFactory.createCommand("Later", task);
    }

    //завершить
    private static Command createFinishCommand(Task task) {
        return ClientCommandFactory.createCommand("Finish", task);
    }

    //отменить(ставится статус отменена)
    private static Command createCancelCommand(Task task) {
        return ClientCommandFactory.createCommand("Cancel", task);
    }

    public static void sendAddCommand(Task task, OutputStream out) {
        try {
            System.out.println("Sending command...");
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createAddCommand(task), out);
            out.flush();
            System.out.println("Sending success");
        }
        catch(JAXBException | IOException e){
            e.printStackTrace();
            e.getMessage();
        }
    }

    public static void sendEditCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createEditCommand(task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.printStackTrace();
        }
    }

    public static void sendDeleteCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createDeleteCommand(task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public static void sendLaterCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createLaterCommand(task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public static void sendFinishCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createFinishCommand(task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public static void sendCancelCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createCancelCommand(task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }
}
