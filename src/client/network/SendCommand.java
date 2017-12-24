package client.network;

import client.commandprocessor.ClientCommandProcessor;
import client.commandprocessor.Command;
import client.model.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class SendCommand {

    public static void sendAddCommand(Task task, OutputStream out) {
        try {
            System.out.println("Sending command...");
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(ClientCommandProcessor.createAddCommand(task), out);

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
            marshaller.marshal(ClientCommandProcessor.createEditCommand(task), out);
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
            marshaller.marshal(ClientCommandProcessor.createDeleteCommand(task), out);
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
            marshaller.marshal(ClientCommandProcessor.createLaterCommand(task), out);
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
            marshaller.marshal(ClientCommandProcessor.createFinishCommand(task), out);
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
            marshaller.marshal(ClientCommandProcessor.createCancelCommand(task), out);
            out.flush();
        }
        catch(JAXBException | IOException e){
            e.getMessage();
        }
    }

    public static void sendSignInCommand(String login, String password, OutputStream out) throws JAXBException, IOException {

        JAXBContext context = JAXBContext.newInstance(Command.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(ClientCommandProcessor.createSignInCommand(login, password), out);
        out.flush();


    }

    public static void sendSignUpCommand(String login, String password, OutputStream out) throws JAXBException, IOException {

        JAXBContext context = JAXBContext.newInstance(Command.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(ClientCommandProcessor.createSignUpCommand(login, password), out);
        out.flush();

    }
}
