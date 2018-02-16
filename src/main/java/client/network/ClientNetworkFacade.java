package client.network;

import auxiliaryclasses.MessageBox;
import client.commandprocessor.ClientCommandSender;
import client.commandprocessor.Command;
import client.commandprocessor.ClientCommandParser;
import auxiliaryclasses.ConstantsClass;
import server.exceptions.UnsuccessfulCommandActionException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Facade for connection with server
 */

public class ClientNetworkFacade {
    private int notificationPort;
    private int serverPort;
    private boolean successConnect;
    private boolean successAuthorization;
    private Socket clientDataSocket;
    private Socket notificationSenderSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ServerSocket notificationSocket;
    private DataInputStream notificationInputStream;
    private static ClientNetworkFacade instance;
    private ClientCommandParser commandParser = ClientCommandParser.getInstance();
    private ClientNotificationListener clientNotificationListener;
    private MessageBox messageBox = MessageBox.getInstance();

    private ClientNetworkFacade() {
        successConnect = false;
    }

    public static ClientNetworkFacade getInstance() {
        if (instance == null) instance = new ClientNetworkFacade();
        return instance;
    }

    /**
     * Connects to server, creates data channels and call method for create notification channels with listener
     *
     * @return 0 or 1 (success or not success connection)
     */

    public int connect() {
        try {
            if (successConnect)
                return 0;
            serverPort = ConstantsClass.DEFAULT_SERVER_PORT;
            clientDataSocket = new Socket("localhost", serverPort);
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            while (dataInputStream.available() < 0) {
                try {
                    Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);
                } catch (InterruptedException e) {
                    messageBox.showMessage(ConstantsClass.CLIENT_CRASH_MESSAGE);
                }
            }
            notificationPort = dataInputStream.readInt();
            System.out.println("Port: " + notificationPort);
            createNotificationChanel(notificationPort);
            return 0;
        } catch (IOException e) {
            messageBox.showMessage(ConstantsClass.SERVER_IS_NOT_AVAILABLE);
        }
        return 1;
    }

    private void createNotificationChanel(int port) {
        try {
            System.out.println("Creating chanel for Notifications");
            notificationSocket = new ServerSocket(port);
            notificationSenderSocket = notificationSocket.accept();
            System.out.println("Connection accepted.");
            notificationInputStream = new DataInputStream(notificationSenderSocket.getInputStream());
            System.out.println("Notification InputStream created");
            clientNotificationListener = new ClientNotificationListener(notificationInputStream);
            clientNotificationListener.start();
            successConnect = true;
        } catch (IOException e) {
            messageBox.showMessage(ConstantsClass.CLIENT_CRASH_MESSAGE);
        }
    }

    /**
     * Finishes facade and closes all channels
     */

    public void finish() {
        try {
            ClientCommandSender.getInstance().sendDisconnectCommand(dataOutputStream);
            clientNotificationListener.interrupt();
            notificationSenderSocket.close();
            dataInputStream.close();
            dataOutputStream.close();
            clientDataSocket.close();
            System.out.println("Closing notification connections & channels - DONE.");
        } catch (UnsuccessfulCommandActionException e) {
            messageBox.showMessage(ConstantsClass.UNSAFE_FINISH);
        } catch (IOException e) {
            messageBox.showMessage(ConstantsClass.CRASH_FINISH);
        }
    }

    /**
     * Waits success authorization command from server
     */

    public void callCommandAuthorizationRelay() {
        try {
            while (!successAuthorization) {
                Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);
                if (dataInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[dataInputStream.available()];
                    int tmp_trash = dataInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.print("Server send: ");
                    System.out.println(command);
                    commandParser.doCommandAction(command);
                }
            }
        } catch (IOException | InterruptedException e) {
            messageBox.showMessage(ConstantsClass.CLIENT_CRASH_MESSAGE);
        }
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public int getNotificationPort() {
        return notificationPort;
    }

    public void setSuccessAuthorization() {
        successAuthorization = true;
    }
}
