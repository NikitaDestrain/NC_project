package server.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkFacade extends Thread {
    private static int DEFAULT_PORT = 1337;
    private int serverPort;
    private ServerSocket serverDataSocket;
    private Socket clientDataSocket;
    private ExecutorService executeIt;

    @Override
    public void run() {
        System.out.println("Server logs:");
        serverPort = DEFAULT_PORT;
        start(serverPort, 20);

        Scanner scanner = new Scanner(System.in);
        while (!serverDataSocket.isClosed()) {
            try {
                clientDataSocket = serverDataSocket.accept();
                executeIt.execute(new MonoClientThread(clientDataSocket));
                if (scanner.nextLine().equalsIgnoreCase("stop"))
                    serverDataSocket.close();
            }
            catch (IOException e) {
                e.getMessage();
            }
        }
        executeIt.shutdown();
    }

    private void start(int port, int nThreads) {
        try {
            executeIt = Executors.newFixedThreadPool(nThreads);
            serverDataSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.getMessage();
        }
    }
}
