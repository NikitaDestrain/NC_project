package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.UserContainer;
import client.gui.authforms.AuthForm;
import client.gui.authforms.SignUpForm;
import client.gui.mainform.MainForm;
import client.network.ClientNetworkFacade;
import server.model.Journal;

public class SuccessfulAuthCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ClientNetworkFacade.getInstance().setSuccessAuthorization();
        MainForm mainForm = MainForm.getInstance();
        if (mainForm == null)
            mainForm = new MainForm();
        mainForm.setUsername(UserContainer.getInstance().getUsername());
        AuthForm authForm = AuthForm.getInstance();
        if (authForm != null)
            authForm.dispose();
        SignUpForm signUpForm = SignUpForm.getInstance();
        if (signUpForm != null)
            signUpForm.dispose();
        Journal journal = (Journal) command.getObject();
        if (journal != null)
            mainForm.setJournal(journal);
        mainForm.setVisible(true);
    }
}
