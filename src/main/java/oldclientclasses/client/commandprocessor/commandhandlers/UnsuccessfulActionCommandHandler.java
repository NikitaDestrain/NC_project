package oldclientclasses.client.commandprocessor.commandhandlers;

import oldclientclasses.client.commandprocessor.Command;

import javax.swing.*;

public class UnsuccessfulActionCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        JOptionPane.showMessageDialog(null, command.getObject().toString(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
