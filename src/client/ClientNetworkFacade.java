package client;

import server.factories.TaskFactory;
import server.model.Task;
import server.model.TaskStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ClientNetworkFacade {
    private static int notificationPort;

    public static void main(String[] args) throws IOException, JAXBException {
        System.out.println("Client logs:");
        notificationPort = PortGenerator.getInstance().createPort();
        Socket socket = new Socket("localhost", 1337);
        OutputStream dos = new DataOutputStream(socket.getOutputStream());
        System.out.println("DataOutputStream created");
        InputStream dis = new DataInputStream(socket.getInputStream());
        System.out.println("DataInputStream created");
        System.out.println("Port: " + notificationPort);
        dos.write(notificationPort);
        dos.flush();

        System.out.println("Creating chanel for Notifications");
        try (ServerSocket clientServer = new ServerSocket(notificationPort)) {
            Socket client = clientServer.accept();
            System.out.println("Connection accepted.");
            OutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("Notification OutputStream  created");
            InputStream in = new DataInputStream(client.getInputStream());
            System.out.println("Notification InputStream created");

            //test
            Task task;
            JAXBContext context = JAXBContext.newInstance(Task.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            task = (Task) unmarshaller.unmarshal(dis);
            System.out.println(task);

            in.close();
            out.close();
            client.close();
            System.out.println("Closing notification connections & channels - DONE.");
        }
    }
}
