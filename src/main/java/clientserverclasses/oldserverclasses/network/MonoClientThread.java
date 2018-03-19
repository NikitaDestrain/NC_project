package clientserverclasses.oldserverclasses.network;

import auxiliaryclasses.ConstantsClass;
import clientserverclasses.oldserverclasses.commandproccessor.Command;
import clientserverclasses.oldserverclasses.commandproccessor.ServerCommandParser;
import clientserverclasses.oldserverclasses.commandproccessor.ServerCommandSender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Thread for work with the clientserverclasses.oldclientclasses.client
 */

public class MonoClientThread extends Thread {

    private int notificationPort;
    private int number;
    private boolean successNotificationConnect;
    private Socket clientDataSocket;
    private Socket notificationSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream notificationOutputStream;
    private ServerCommandParser commandParser = ServerCommandParser.getInstance();
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();
    private StreamContainer streamContainer = StreamContainer.getInstance();

    public MonoClientThread(Socket socket, int notificationPort) {
        this.clientDataSocket = socket;
        this.notificationPort = notificationPort;
        this.number = notificationPort;
        this.successNotificationConnect = true;
    }

    @Override
    public void run() {
        System.out.printf("\nConnection accepted.\n");
        System.out.printf("Client with port %d connected\n", number);
        init();
        connectToNotificationChanel();
        if (successNotificationConnect)
            commandRelay();
        else
            finish();
    }

    private void init() {
        try {
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream  created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            streamContainer.addClientDataOutputStreams(notificationPort, dataOutputStream);
        } catch (IOException e) {
            System.out.printf("Error! Client with port %d can not be connected!\n", notificationPort);
        }
    }

    private void connectToNotificationChanel() {
        System.out.println("Creating Notification Chanel");
        try {
            dataOutputStream.writeInt(notificationPort);
            dataOutputStream.flush();
            notificationSocket = new Socket("localhost", notificationPort);
            notificationOutputStream = new DataOutputStream(notificationSocket.getOutputStream());
            System.out.println("Notification OutputStream created");
            streamContainer.addNotificationOutputStream(notificationPort, notificationOutputStream);
        } catch (IOException e) {
            commandSender.sendUnsuccessfulActionCommand("Error! Server can not connect to notification channels! Application has been finished!", dataOutputStream);
            successNotificationConnect = false;
        }
    }

    /**
     * Finishes clientserverclasses.oldclientclasses.client's thread and close all channels
     */

    public void finish() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            notificationOutputStream.close();
            clientDataSocket.close();
            notificationSocket.close();
            streamContainer.removeNotificationOutputStream(notificationPort);
            streamContainer.removeClientDataOutputStreams(notificationPort);
        } catch (IOException e) {
            streamContainer.removeNotificationOutputStream(notificationPort);
            streamContainer.removeClientDataOutputStreams(notificationPort);
        }
    }

    private void commandRelay() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);
                if (dataInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[dataInputStream.available()];
                    int tmp_trash = dataInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.printf("Client with port %d send: ", number);
                    System.out.println(command);
                    if (commandParser.doCommandAction(command) == 1)
                        commandSender.sendUnsuccessfulActionCommand(ConstantsClass.UNKNOWN_COMMAND, dataOutputStream);
                }
            }
        } catch (IOException | InterruptedException e) {
            finish();
        }
    }
}
