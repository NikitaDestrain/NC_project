package server.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetworkFacade {
    public static void main(String[] args) throws IOException {
        System.out.println("Server logs:");
        try (ServerSocket server = new ServerSocket(1337)) {
            Socket client = server.accept();
            System.out.println("Connection accepted.");
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");

            System.out.println("Creating Notif Chanel");
            //todo fix it later!
            int port = in.read() + 768; //выведено опытным путем
            System.out.println(port);
            Socket socket = new Socket("localhost", port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Notification OutputStream created");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            System.out.println("Notification InputStream created");

            in.close();
            out.close();
            client.close();
            System.out.println("Closing connections & channels - DONE.");
        }
    }
}
