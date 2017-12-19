package server.controller;

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
import java.util.Scanner;

public class ServerNetworkFacade {
    public static void main(String[] args) throws IOException, JAXBException, InterruptedException {
        System.out.println("Server logs:");
        while (true) {
            try (ServerSocket server = new ServerSocket(1337)) {
                Socket client = server.accept();
                System.out.println("Connection accepted.");
                OutputStream out = new DataOutputStream(client.getOutputStream());
                System.out.println("DataOutputStream  created");
                InputStream in = new DataInputStream(client.getInputStream());
                System.out.println("DataInputStream created");

                System.out.println("Creating Notification Chanel");
                //todo fix it later!
                int port = in.read() + 768; //выведено опытным путем
                System.out.println(port);
                Socket socket = new Socket("localhost", port);
                OutputStream dos = new DataOutputStream(socket.getOutputStream());
                System.out.println("Notification OutputStream created");
                InputStream dis = new DataInputStream(socket.getInputStream());
                System.out.println("Notification InputStream created");

                Thread.sleep(3000);
                //test
                Task task = TaskFactory.createTask("test", TaskStatus.Planned, "dsd", new Date(), new Date());
                JAXBContext context = JAXBContext.newInstance(Task.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(task, out);

                in.close();
                out.close();
                client.close();
                System.out.println("Closing connections & channels - DONE.");
            }
            System.out.println();
            System.out.print("Command: ");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if(command.equalsIgnoreCase("stop"))
                break;
            System.out.println("Waiting...");
        }
    }
}
