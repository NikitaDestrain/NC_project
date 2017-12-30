package server.network;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandParser;
import server.commandproccessor.ServerCommandSender;
import server.controller.Controller;
import server.model.Task;
import server.model.TaskStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * Thread for work with the client
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
    private DataInputStream notificationInputStream;
    private ServerCommandParser commandParser = ServerCommandParser.getInstance();
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();
    private ServerNetworkFacade serverNetworkFacade = ServerNetworkFacade.getInstance();
    private ServerNotificationListener serverNotificationListener;
    private boolean stopCommandRelay;

    public MonoClientThread(Socket socket, int notificationPort) {
        this.clientDataSocket = socket;
        this.notificationPort = notificationPort;
        this.number = notificationPort;
        this.successNotificationConnect = true;
        this.stopCommandRelay = false;
    }

    @Override
    public void run() {
        System.out.printf("\nConnection accepted.\n");
        System.out.printf("Client with port %d connected\n", number);
        init();
        connectToNotificationChanel();
        if(successNotificationConnect)
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
            serverNetworkFacade.addClientDataOutputStreams(notificationPort, dataOutputStream);
        }
        catch (IOException e) {
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
            notificationInputStream = new DataInputStream(notificationSocket.getInputStream());
            System.out.println("Notification InputStream created");
            serverNetworkFacade.addNotificationOutputStream(notificationPort, notificationOutputStream);
            serverNotificationListener = new ServerNotificationListener(notificationInputStream, notificationPort);
            serverNotificationListener.start();
        }
        catch (IOException e) {
            commandSender.sendUnsuccessfulActionCommand("Error! Server can not connect to notification channels! Application has been finished!", dataOutputStream);
            successNotificationConnect = false;
        }
    }

    /**
     * Finishes client's thread and close all channels
     */

    protected void finish() {
        try {
            stopCommandRelay = true;
            serverNotificationListener.interrupt();
            dataInputStream.close();
            dataOutputStream.close();
            clientDataSocket.close();
            System.out.printf("Client with port %d disconnected.\n", number);
            serverNetworkFacade.removeNotificationOutputStream(notificationPort);
            serverNetworkFacade.removeClientDataOutputStreams(notificationPort);
        }
        catch (IOException e) {
            serverNetworkFacade.removeNotificationOutputStream(notificationPort);
            serverNetworkFacade.removeClientDataOutputStreams(notificationPort);
        }
    }

    private void commandRelay() {
        try {
            while (true) {
                Thread.sleep(500);
                if(dataInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[dataInputStream.available()];
                    int tmp_trash = dataInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.printf("Client with port %d send: ", number);
                    System.out.println(command);
                    if(commandParser.doCommandAction(command) == 1)
                        commandSender.sendUnsuccessfulActionCommand("Error! Unknown command!", dataOutputStream);
                    if(stopCommandRelay)
                        break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
