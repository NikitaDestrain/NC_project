package client.network;

import client.commandprocessor.CommandSender;
import client.model.Task;
import client.model.TaskStatus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ClientNetworkFacade extends Thread {
    private int notificationPort;
    private int serverPort;
    private Socket clientDataSocket;
    private Socket notificationSenderSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ServerSocket notificationSocket;
    private DataOutputStream notificationOutputStream;
    private DataInputStream notificationInputStream;


    @Override
    public void run() {
        System.out.println("Client logs:");
        System.out.println();
        serverPort = 1337;
        connect(serverPort);
        createNotificationChanel(notificationPort);

        Scanner scanner = new Scanner(System.in);

        DataServerListener datalistener = new DataServerListener(dataInputStream);
        datalistener.start();
        NotificationServerListener notiflistener = new NotificationServerListener(notificationInputStream);
        notiflistener.start();

       while(true) {
            //test
            CommandSender.sendAddCommand(new Task("sss", TaskStatus.Planned, "s", new Date(), new Date(), 0), dataOutputStream);
            if (scanner.nextLine().equalsIgnoreCase("stop")) {
                break;
            }
        }
        System.out.println("Finish.");
       finish();

    }

    private void connect(int port) {
        try {
            clientDataSocket = new Socket("localhost", port);
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            notificationPort = dataInputStream.readInt();
            System.out.println("Port: " + notificationPort);
        }
        catch (IOException e) {
            System.out.println("Server is offline");

        }
    }

    private void createNotificationChanel(int port) {
        try {
            System.out.println("Creating chanel for Notifications");
            notificationSocket = new ServerSocket(port);
            notificationSenderSocket = notificationSocket.accept();
            System.out.println("Connection accepted.");
            notificationOutputStream = new DataOutputStream(notificationSenderSocket.getOutputStream());
            System.out.println("Notification OutputStream  created");
            notificationInputStream = new DataInputStream(notificationSenderSocket.getInputStream());
            System.out.println("Notification InputStream created");
        }
        catch (IOException e){
            e.getMessage();

        }
    }

    private void finish() {
        try {
            notificationInputStream.close();
            notificationOutputStream.close();
            notificationSenderSocket.close();
            System.out.println("Closing notification connections & channels - DONE.");
        }
        catch (IOException e) {
            e.getMessage();
        }
    }


    public DataOutputStream getDataOutputStream()
    {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream()
    {
        return dataInputStream;
    }

    public DataOutputStream getNotificationOutputStream()
    {
        return notificationOutputStream;
    }

    public DataInputStream getNotificationInputStream()
    {
        return notificationInputStream;
    }
}
