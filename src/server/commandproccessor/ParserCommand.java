package server.commandproccessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ParserCommand {

    public static Command parseToCommand (InputStream in) {
        Command command;
        try {
            System.out.println("Start reading command");
            JAXBContext context = JAXBContext.newInstance(Command.class);
            System.out.println("1");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.out.println("2");
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
