package oldclientclasses.client.commandprocessor.commandhandlers;

import oldclientclasses.client.commandprocessor.Command;
import oldclientclasses.client.gui.UserContainer;
import oldclientclasses.client.gui.authforms.AuthForm;
import oldclientclasses.client.gui.authforms.SignUpForm;
import oldclientclasses.client.gui.mainform.MainForm;
import oldclientclasses.client.network.ClientNetworkFacade;
import server.model.Journal;

public class SuccessfulAuthCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        ClientNetworkFacade.getInstance().setSuccessAuthorization();
        /*MainForm mainForm = MainForm.getInstance();
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
        mainForm.setVisible(true);*/
    }
}
