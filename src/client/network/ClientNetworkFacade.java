package client.network;

import client.commandprocessor.ClientCommandProcessor;
import client.model.TaskStatus;
import client.model.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class ClientNetworkFacade {
    private static int notificationPort;
    private static int serverPort;
    private static Socket clientDataSocket;
    private static Socket notificationSenderSocket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static ServerSocket notificationSocket;
    private static DataOutputStream notificationOutputStream;
    private static DataInputStream notificationInputStream;

    public static void main(String[] args) throws InterruptedException{
        System.out.println("Client logs:");
        serverPort = 1337;
        connect(serverPort);
        createNotificationChanel(notificationPort);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            //test
            ClientCommandProcessor.sendAddCommand(new Task("sss", TaskStatus.Planned,"s", new Date(), new Date(), 0), dataOutputStream);
            if(scanner.nextLine().equalsIgnoreCase("stop"))
                break;
        }
        System.out.println("Finish.");
        finish();
    }

    private static void connect(int port) {
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

    private static void createNotificationChanel(int port) {
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

    private static void finish() {
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
}
