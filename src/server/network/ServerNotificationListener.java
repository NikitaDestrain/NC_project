package server.network;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.MessageBox;
import server.commandproccessor.Command;
import server.commandproccessor.ServerCommandParser;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Listener for notification from client
 */

public class ServerNotificationListener extends Thread{

    private DataInputStream notificationInputStream;
    private int port;
    private ServerCommandParser commandParser = ServerCommandParser.getInstance();
    private MessageBox messageBox = MessageBox.getInstance();

    public ServerNotificationListener(DataInputStream in, int port) {
        this.notificationInputStream = in;
        this.port = port;
    }

    public void run() {
        System.out.printf("Notification listener for client with port %d starts\n", port);
        try {
            while (true) {
                Thread.sleep(ConstantsClass.SLEEP_FOR_500_SEC);//todo vlla magic value -> constant DONE
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
            //todo vlla то есть что бы не случилось - мы сразу рвем соединение с клиентом? Не слишком радикально?
            // уточнить!
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            ServerNetworkFacade.getInstance().removeNotificationOutputStream(port);
            System.out.printf("Connection with client (port %d) was closed.\n", port);
        }
    }
}


