package clientserverclasses.oldclientclasses.client.commandprocessor.commandhandlers;

import clientserverclasses.oldclientclasses.client.commandprocessor.Command;
import clientserverclasses.oldclientclasses.client.gui.authforms.AuthForm;

public class UnsuccessfulSignInCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        AuthForm authForm = AuthForm.getInstance();
        if (authForm == null) authForm = new AuthForm();
        authForm.setVisible(true);
        authForm.showUnsuccessfulAuthMessage();
    }
}
