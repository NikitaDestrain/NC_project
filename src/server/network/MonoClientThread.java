package server.network;

import server.commandproccessor.Command;
import server.commandproccessor.ParserCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class MonoClientThread implements Runnable {

    private int notificationPort;
    private static int number = 0;
    private Socket clientDataSocket;
    private Socket notificationSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream notificationOutputStream;
    private DataInputStream notificationInputStream;

    public MonoClientThread(Socket socket) {
        clientDataSocket = socket;
    }

    @Override
    public void run() {
        System.out.printf("\nConnection accepted.");
        System.out.printf("Client №%d is connected\n", ++number);
        start();
        connectToNotificationChanel();
        try {

//sleep (5000);
               while (true) {
                   // читаем данные в буфер
                   if(dataInputStream.available()>0) {
                       byte[] buffer = new byte[dataInputStream.available()];
                       int readBytesCount = dataInputStream.read(buffer);
                       System.out.println("размер прочитанного" + readBytesCount);
                       System.out.println("читаем из потока");
                           Command com = ParserCommand.parseToCommand(buffer);
                           System.out.println(com);
                   }
               }

        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();
    }

    private void start() {
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

    private void connectToNotificationChanel() {
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

    private void finish() {
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
