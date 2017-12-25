package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.authforms.SignUpForm;

public class UnsuccessfulSignUpCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        SignUpForm signUpForm = SignUpForm.getInstance();
        if (signUpForm == null) signUpForm = new SignUpForm();
        signUpForm.setVisible(true);
        signUpForm.showUnsuccessfulSignUpMessage();
    }
}
