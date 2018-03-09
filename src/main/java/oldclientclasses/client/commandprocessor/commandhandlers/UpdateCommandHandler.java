package oldclientclasses.client.commandprocessor.commandhandlers;

import oldclientclasses.client.commandprocessor.Command;
import oldclientclasses.client.gui.mainform.MainForm;
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
