package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.authforms.AuthForm;

public class UnsuccessfulSignInCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        AuthForm authForm = AuthForm.getInstance();
        if (authForm == null) authForm = new AuthForm();
        authForm.setVisible(true);
        authForm.showUnsuccessfulAuthMessage();
    }
}
