package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientNetworkFacade {
    public static void main(String[] args) throws IOException{
        System.out.println("Client logs:");
        Socket socket = new Socket("localhost", 1337);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        System.out.println("DataOutputStream created");
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        System.out.println("DataInputStream created");
        try (ServerSocket clientServer = new ServerSocket(1338)) {
            Socket client = clientServer.accept();
            System.out.print("Connection accepted.");
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("NotifOutputStream  created");
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("NotifInputStream created");
            in.close();
            out.close();
            client.close();
            System.out.println("Closing notif connections & channels - DONE.");
        }
    }
}
