package server.controller;

import javax.xml.bind.JAXB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetworkFacade {
    public static void main(String[] args) throws IOException{
        System.out.println("Server logs:");
        try (ServerSocket server = new ServerSocket(1337)) {
            Socket client = server.accept();
            System.out.print("Connection accepted.");
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");
            System.out.println("Creating Notif Chanel");
            Socket socket = new Socket("localhost", 1338);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("NotifStream created");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            System.out.println("NotifInputStream created");
            in.close();
            out.close();
            client.close();
            System.out.println("Closing connections & channels - DONE.");
        }
    }
}
