package server.network;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkFacade {
    private static int DEFAULT_PORT = 1337;
    private static int serverPort;
    private static ServerSocket serverDataSocket;
    private static Socket clientDataSocket;
    private static ExecutorService executeIt;

    public static void main(String[] args) throws IOException, JAXBException, InterruptedException {
        System.out.println("Server logs:");
        serverPort = DEFAULT_PORT;
        start(serverPort, 20);

        Scanner scanner = new Scanner(System.in);
        while (!serverDataSocket.isClosed()) {
            clientDataSocket = serverDataSocket.accept();
            executeIt.execute(new MonoClientThread(clientDataSocket));
            System.out.println("Connection accepted.");
            if(scanner.nextLine().equalsIgnoreCase("stop"))
                serverDataSocket.close();
        }
        executeIt.shutdown();
    }

    private static void start(int port, int nThreads) {
        try {
            executeIt = Executors.newFixedThreadPool(nThreads);
            serverDataSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.getMessage();
        }
    }
}
