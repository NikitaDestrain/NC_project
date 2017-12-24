package client.commandprocessor;



import client.commandprocessor.Command;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class ParserCommand {

    public static Command parseToCommand (InputStream in) {
        Command command;
        try {
            System.out.println("Start reading command");
            JAXBContext context = JAXBContext.newInstance(Command.class);
            System.out.println("1");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.out.println("2");
            try {
                System.out.println(in.available());
            } catch (IOException e) {
                e.printStackTrace();
            }

            command = (Command) unmarshaller.unmarshal(in);




            System.out.println("Command reading success");

        } catch (JAXBException e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println("Parse error!");
            return null;
        }
        return command;


    }
}