package oldclientclasses.client.commandprocessor.commandhandlers;

import oldclientclasses.client.commandprocessor.Command;
import oldclientclasses.client.gui.authforms.SignUpForm;

public class UnsuccessfulSignUpCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        SignUpForm signUpForm = SignUpForm.getInstance();
        if (signUpForm == null) signUpForm = new SignUpForm();
        signUpForm.setVisible(true);
        signUpForm.showUnsuccessfulSignUpMessage();
    }
}
