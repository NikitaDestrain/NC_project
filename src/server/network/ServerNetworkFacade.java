package server.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkFacade extends Thread {
    private static final int DEFAULT_PORT = 1337;
    private static final int DEFAULT_MAX_CNT_CLIENTS = 20;
    private static final int DEFAULT_CURRENT_CNT_CLIENTS = 0;
    private int serverPort;
    private ServerSocket serverDataSocket;
    private Socket clientDataSocket;
    private ExecutorService executeIt;
    private Map<Integer, DataOutputStream> clientNotificationOutputStreams;
    private Map<Integer, DataOutputStream> clientDataInputStreams;
    private static ServerNetworkFacade instance;
    private int clientCount;

    private ServerNetworkFacade() {}

    public static ServerNetworkFacade getInstance() {
        if (instance == null) instance = new ServerNetworkFacade();
        return instance;
    }

    @Override
    public void run() {
        System.out.println("Server logs:");
        serverPort = DEFAULT_PORT;
        start(serverPort, DEFAULT_MAX_CNT_CLIENTS);

        Scanner scanner = new Scanner(System.in);
        while (!serverDataSocket.isClosed()) {
            try {
                clientDataSocket = serverDataSocket.accept();
                int clientNotificationPort = PortGenerator.getInstance().createPort();
                MonoClientThread monoClientThread = new MonoClientThread(clientDataSocket, clientNotificationPort);
                executeIt.execute(monoClientThread);
                clientCount++;
                /*if (scanner.nextLine().equalsIgnoreCase("stop"))
                    serverDataSocket.close();*/
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
            clientCount = DEFAULT_CURRENT_CNT_CLIENTS;
            clientNotificationOutputStreams = new HashMap<>();
            clientDataInputStreams = new HashMap<>();
            serverDataSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.getMessage();
        }
    }

    public LinkedList<DataOutputStream> getClientNotificationOutputStreams() {
        return new LinkedList<>(clientNotificationOutputStreams.values());
    }

    public LinkedList<DataOutputStream> getClientDataInputStreams() {
        return new LinkedList<>(clientDataInputStreams.values());
    }

    public void removeNotificationOutputStream(Integer key) {
        clientNotificationOutputStreams.remove(key);
    }

    public void addNotificationOutputStream(Integer key, DataOutputStream dataOutputStream) {
        clientNotificationOutputStreams.put(key, dataOutputStream);
    }

    public void addClientDataInputStreams(Integer key, DataOutputStream dataOutputStream) {
        clientDataInputStreams.put(key, dataOutputStream);
    }

    public void removeClientDataInputStreams(Integer key) {
        clientDataInputStreams.remove(key);
        --clientCount;
    }
}
