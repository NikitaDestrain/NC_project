package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientNetworkFacade {
    private static int notificationPort;

    public static void main(String[] args) throws IOException {
        System.out.println("Client logs:");
        notificationPort = PortGenerator.getInstance().createPort();
        Socket socket = new Socket("localhost", 1337);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        System.out.println("DataOutputStream created");
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        System.out.println("DataInputStream created");
        System.out.println("Port: " + notificationPort);
        dos.write(notificationPort);
        dos.flush();

        System.out.println("Creating chanel for Notifications");
        try (ServerSocket clientServer = new ServerSocket(notificationPort)) {
            Socket client = clientServer.accept();
            System.out.println("Connection accepted.");
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("Notification OutputStream  created");
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("Notification InputStream created");

            in.close();
            out.close();
            client.close();
            System.out.println("Closing notification connections & channels - DONE.");
        }
    }
}
