package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;
import client.gui.UserContainer;
import client.gui.authforms.AuthForm;
import client.gui.authforms.SignUpForm;
import client.gui.mainform.MainForm;

public class SuccessfulAuthCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        MainForm mainForm = MainForm.getInstance();
        if (mainForm == null) mainForm = new MainForm();
        mainForm.setUsername(UserContainer.getInstance().getUsername());
        AuthForm authForm = AuthForm.getInstance();
        if (authForm != null) authForm.dispose();
        SignUpForm signUpForm = SignUpForm.getInstance();
        if (signUpForm != null) signUpForm.dispose();
        mainForm.setVisible(true);
    }
}
