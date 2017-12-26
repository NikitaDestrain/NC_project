package server.network;

import server.commandproccessor.Command;
import server.commandproccessor.CommandParser;
import server.commandproccessor.CommandSender;
import server.controller.Controller;
import server.model.Task;
import server.model.TaskStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import static java.lang.Thread.sleep;

public class MonoClientThread extends Thread {

    private int notificationPort;
    private int number;
    private Socket clientDataSocket;
    private Socket notificationSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream notificationOutputStream;
    private DataInputStream notificationInputStream;
    private CommandParser commandParser = CommandParser.getInstance();
    private ServerNetworkFacade serverNetworkFacade = ServerNetworkFacade.getInstance();

    public MonoClientThread(Socket socket, int notificationPort) {
        this.clientDataSocket = socket;
        this.notificationPort = notificationPort;
        this.number = notificationPort;
    }

    @Override
    public void run() {
        System.out.printf("\nConnection accepted.\n");
        System.out.printf("Client with port %d is connected\n", number);
        init();
        connectToNotificationChanel();
        //todo test it
        //CommandSender.getInstance().sendUpdateCommand(Controller.getInstance().getJournal(), dataOutputStream);
        //CommandSender.getInstance().sendNotificationCommand(new Task("sds", TaskStatus.Planned, "sfsaf", new Date(), new Date(), 1), notificationOutputStream);
        commandRelay();
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
            e.getMessage();
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
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    private void finish() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            clientDataSocket.close();
            System.out.printf("Client with port %d is disconnected", notificationPort);
            serverNetworkFacade.removeNotificationOutputStream(notificationPort);
            serverNetworkFacade.removeClientDataOutputStreams(notificationPort);
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    private void commandRelay() {
        try {
            while (true) {
                if(dataInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[dataInputStream.available()];
                    int tmp_trash = dataInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.printf("Client №%d send: ", number);
                    System.out.println(command);
                    if(commandParser.doCommandAction(command) == 1)
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
