package client.commandprocessor;


import client.model.Task;
import server.gui.mainform.MainForm;
import server.gui.notificationwindow.NotificationForm;
import server.model.Journal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class ParserCommand {

    public static Command parseToCommand (InputStream in) {
        Command command;
        try {
            System.out.println("Start reading command");
            JAXBContext context = JAXBContext.newInstance(server.commandproccessor.Command.class);
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

    public static void doCommandAction(Command command) {
        if (command != null) {
            switch (command.getName()) {
                case "Update" :
                    MainForm mainForm = MainForm.getInstance();
                    if (mainForm == null) mainForm = new MainForm();
                    mainForm.setJournal((Journal) command.getObject()); // при update приходит журнал
                    break;
                case "Notification" :
                    new NotificationForm().setTask((server.model.Task) command.getObject());
                    break;
            }
        }
    }
}
