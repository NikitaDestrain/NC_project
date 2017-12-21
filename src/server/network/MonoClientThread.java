package server.network;

import server.commandproccessor.ParserCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MonoClientThread implements Runnable{

    private static int notificationPort;
    private static int number = 0;
    private static Socket clientDataSocket;
    private static Socket notificationSocket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static DataOutputStream notificationOutputStream;
    private static DataInputStream notificationInputStream;

    public MonoClientThread(Socket socket) {
        clientDataSocket = socket;
    }

    @Override
    public void run() {
        System.out.printf("\nClient №%d is connected\n", ++number);
        start();
        connectToNotificationChanel();
        System.out.println(ParserCommand.parseToCommand(dataInputStream));
        finish();
    }

    private static void start() {
        try {
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream  created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    private static void connectToNotificationChanel() {
        System.out.println("Creating Notification Chanel");
        notificationPort = PortGenerator.getInstance().createPort();
        System.out.println("Port: " + notificationPort);
        try {
            dataOutputStream.writeInt(notificationPort);
            dataOutputStream.flush();
            notificationSocket = new Socket("localhost", notificationPort);
            notificationOutputStream = new DataOutputStream(notificationSocket.getOutputStream());
            System.out.println("Notification OutputStream created");
            notificationInputStream = new DataInputStream(notificationSocket.getInputStream());
            System.out.println("Notification InputStream created");
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    private static void finish() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            clientDataSocket.close();
            System.out.println("Closing connections & channels - DONE.");
        }
        catch (IOException e) {
            e.getMessage();
        }
    }
}
