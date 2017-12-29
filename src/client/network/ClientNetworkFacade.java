package client.network;

import client.commandprocessor.ClientCommandSender;
import client.commandprocessor.Command;
import client.commandprocessor.ClientCommandParser;
import constants.ConstantsClass;
import server.gui.mainform.MainForm;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientNetworkFacade extends Thread {
    private int notificationPort;
    private int serverPort;
    private boolean successConnect;
    private Socket clientDataSocket;
    private Socket notificationSenderSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ServerSocket notificationSocket;
    private DataOutputStream notificationOutputStream;
    private DataInputStream notificationInputStream;
    private static ClientNetworkFacade instance;
    private ClientCommandParser commandParser = ClientCommandParser.getInstance();
    private ClientNotificationListener clientNotificationListener;

    private ClientNetworkFacade() {
        successConnect = false;
    }

    public static ClientNetworkFacade getInstance() {
        if (instance == null) instance = new ClientNetworkFacade();
        return instance;
    }

    @Override
    public void run() {
        System.out.println("Client logs:");
        System.out.println();
        while(true) {
            try {
                Thread.sleep(ConstantsClass.SLEEP_FOR_250_SEC);
                if (successConnect)
                    break;
            } catch (InterruptedException e) {}
        }
        commandRelay();
    }

    public int connect() {
        try {
            if(successConnect)
                return 0;
            serverPort = ConstantsClass.DEFAULT_SERVER_PORT;
            clientDataSocket = new Socket("localhost", serverPort);
            dataOutputStream = new DataOutputStream(clientDataSocket.getOutputStream());
            System.out.println("DataOutputStream created");
            dataInputStream = new DataInputStream(clientDataSocket.getInputStream());
            System.out.println("DataInputStream created");
            while(true) {
                try {
                    Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);
                    if (dataInputStream.available() > 0)
                        break;
                }
                catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(MainForm.getInstance(), "Something is going wrong! For correct work you should restart application!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            notificationPort = dataInputStream.readInt();
            System.out.println("Port: " + notificationPort);
            createNotificationChanel(notificationPort);
            return 0;
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Server is not available! Try later!", "Connection error", JOptionPane.ERROR_MESSAGE);
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
            clientNotificationListener = new ClientNotificationListener(notificationInputStream);
            clientNotificationListener.start();
            successConnect = true;
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(MainForm.getInstance(), "Something is going wrong! For correct work you should restart application!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void finish() {
        try {
            ClientCommandSender.getInstance().sendDisconnectCommand(dataOutputStream);
            clientNotificationListener.interrupt();
            notificationInputStream.close();
            notificationOutputStream.close();
            notificationSenderSocket.close();
            System.out.println("Closing notification connections & channels - DONE.");
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(MainForm.getInstance(), "Crush finish! Something is going wrong!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void commandRelay() {
        try {
            while (true) {
                Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);
                if(dataInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[dataInputStream.available()];
                    int tmp_trash = dataInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.print("Server send: ");
                    System.out.println(command);
                    commandParser.doCommandAction(command);
                }
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(MainForm.getInstance(), "Something is going wrong! For correct work you should restart application!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public DataOutputStream getDataOutputStream()
    {
        return dataOutputStream;
    }

    public DataOutputStream getNotificationOutputStream()
    {
        return notificationOutputStream;
    }

    public int getNotificationPort() { return notificationPort; }
}
