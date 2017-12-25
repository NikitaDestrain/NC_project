package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.UserContainer;
import client.gui.mainform.MainForm;

public class SuccessfulAuthCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        MainForm mainForm = MainForm.getInstance();
        if (mainForm == null) mainForm = new MainForm();
        mainForm.setUsername(UserContainer.getInstance().getUsername());
        mainForm.setVisible(true);
    }
}
