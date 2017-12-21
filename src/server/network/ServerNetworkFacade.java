package server.network;

import server.commandproccessor.Command;
import server.commandproccessor.ParserCommand;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetworkFacade {
    private static int DEFAULT_PORT = 1337;
    private static int serverPort;
    private static int notificationPort;
    private static ServerSocket serverDataSocket;
    private static Socket clientDataSocket;
    private static Socket notificationSocket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static DataOutputStream notificationOutputStream;
    private static DataInputStream notificationInputStream;

    public static void main(String[] args) throws IOException, JAXBException, InterruptedException {
        System.out.println("Server logs:");
        serverPort = DEFAULT_PORT;
        start(serverPort);
        connectToNotificationChanel();

        while (true) {
            //test
            Command command = ParserCommand.parseToCommand(dataInputStream);
            System.out.println(command);
            System.out.println("Finish");
            break;
        }
        finish();
    }


    private static void start(int port) {
        try {
            serverDataSocket = new ServerSocket(port);
            clientDataSocket = serverDataSocket.accept();
            System.out.println("Connection accepted.");
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
