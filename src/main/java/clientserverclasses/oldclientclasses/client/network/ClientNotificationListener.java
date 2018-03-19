package clientserverclasses.oldclientclasses.client.network;

import auxiliaryclasses.ConstantsClass;
import clientserverclasses.MessageBox;
import clientserverclasses.oldclientclasses.client.commandprocessor.ClientCommandParser;
import clientserverclasses.oldclientclasses.client.commandprocessor.Command;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Listener for notification from server
 */

public class ClientNotificationListener extends Thread {

    private DataInputStream notificationInputStream;
    private ClientCommandParser commandParser = ClientCommandParser.getInstance();
    private MessageBox messageBox = MessageBox.getInstance();

    public ClientNotificationListener(DataInputStream in) {
        this.notificationInputStream = in;
    }

    public void run() {
        System.out.println("Notification listener starts");
        try {
            while (!isInterrupted()) {
                Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);
                if (notificationInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[notificationInputStream.available()];
                    int tmp_trash = notificationInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.print("Server send: ");
                    System.out.println(command);
                    commandParser.doCommandAction(command);
                }
            }
            closeInputStream();
        } catch (IOException e) {
            messageBox.showMessage(ConstantsClass.CLIENT_CRASH_MESSAGE);
        } catch (InterruptedException e) {
            closeInputStream();
        }
    }

    private void closeInputStream() {
        try {
            notificationInputStream.close();
        } catch (IOException e) {
            messageBox.showMessage(ConstantsClass.CLIENT_CRASH_MESSAGE);
        }
    }
}

