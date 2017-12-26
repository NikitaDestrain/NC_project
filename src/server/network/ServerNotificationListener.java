package server.network;

import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandParser;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerNotificationListener extends Thread{

    private DataInputStream notificationInputStream;
    private int port;
    private ServerCommandParser commandParser = ServerCommandParser.getInstance();

    public ServerNotificationListener(DataInputStream in, int port) {
        this.notificationInputStream = in;
        this.port = port;
    }

    public void run() {
        System.out.printf("Notification listener for client with port %d starts\n", port);
        try {
            while (true) {
                if(notificationInputStream.available() > 0) {
                    byte[] tmp_buffer = new byte[notificationInputStream.available()];
                    int tmp_trash = notificationInputStream.read(tmp_buffer);
                    Command command = commandParser.parseToCommand(tmp_buffer);
                    System.out.print("Client send: ");
                    System.out.println(command);
                    commandParser.doCommandAction(command);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


