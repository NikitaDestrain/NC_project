package client.network;

import client.commandprocessor.Command;
import client.commandprocessor.CommandParser;
import client.commandprocessor.CommandSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientNetworkFacade extends Thread {
    private static final int DEFAULT_SERVER_PORT = 1337;
    private int notificationPort;
    private int serverPort;
    private Socket clientDataSocket;
    private Socket notificationSenderSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ServerSocket notificationSocket;
    private DataOutputStream notificationOutputStream;
    private DataInputStream notificationInputStream;
    private static ClientNetworkFacade instance;
    private CommandSender commandSender = CommandSender.getInstance();
    private CommandParser commandParser = CommandParser.getInstance();

    private ClientNetworkFacade() {}

    public static ClientNetworkFacade getInstance() {
        if (instance == null) instance = new ClientNetworkFacade();
        return instance;
    }

    @Override
    public void run() {
        System.out.println("Client logs:");
        System.out.println();
        serverPort = DEFAULT_SERVER_PORT;
        Scanner scanner = new Scanner(System.in);
        //временное
        while(true) {
            if(connect(serverPort) == 0)
                break;
            System.out.print("Write \"1\" for reconnect:");
            if(!scanner.nextLine().equalsIgnoreCase("1"))
                break;
            System.out.println();
        }
        createNotificationChanel(notificationPort);
        commandRelay();
    }

    private int connect(int port) {
        try {
            clientDataSocket = new Socket("localhost", port);
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            while(true) {
                if(dataInputStream.available() > 0)
                    break;
            }
            notificationPort = dataInputStream.readInt();
            System.out.println("Port: " + notificationPort);
            return 0;
        }
        catch (IOException e) {
            System.out.println("Server is offline!");
        }
        return 1;
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

    public void finish() {
        try {
            CommandSender.getInstance().sendDisconnectCommand(dataOutputStream);
            notificationInputStream.close();
            notificationOutputStream.close();
            notificationSenderSocket.close();
            System.out.println("Closing notification connections & channels - DONE.");
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
                    System.out.print("Server send: ");
                    System.out.println(command);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public int getNotificationPort() { return notificationPort; }


}
