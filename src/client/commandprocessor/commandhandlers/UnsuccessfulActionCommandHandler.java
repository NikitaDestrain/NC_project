package client.commandprocessor.commandhandlers;

import client.commandprocessor.Command;

import javax.swing.*;

public class UnsuccessfulActionCommandHandler implements CommandHandler {
    @Override
    public synchronized void handle(Command command) {
        JOptionPane.showMessageDialog(null, "Could not perform this action!",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
