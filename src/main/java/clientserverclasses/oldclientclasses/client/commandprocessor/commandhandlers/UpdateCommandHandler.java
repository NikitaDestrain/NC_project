package clientserverclasses.oldclientclasses.client.commandprocessor.commandhandlers;

import clientserverclasses.oldclientclasses.client.commandprocessor.Command;
import clientserverclasses.oldclientclasses.client.gui.mainform.MainForm;
import server.model.Journal;

public class UpdateCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        /*MainForm mainForm = MainForm.getInstance();
        if (mainForm == null) mainForm = new MainForm();
        mainForm.setJournal((Journal) command.getObject());
        mainForm.setVisible(true);*/
    }
}
