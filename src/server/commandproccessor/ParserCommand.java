package server.commandproccessor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class ParserCommand {

    public static Command parseToCommand (InputStream in) {
        Command command;
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            command = (Command) unmarshaller.unmarshal(in);
        } catch (JAXBException e) {
            System.out.println("Parse error!");
            return null;
        }
        return command;
    }
}
