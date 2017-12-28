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
    private Map<Integer, DataOutputStream> clientDataOutputStreams;
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
        while (!serverDataSocket.isClosed()) {
            try {
                clientDataSocket = serverDataSocket.accept();
                int clientNotificationPort = PortGenerator.getInstance().createPort();
                MonoClientThread monoClientThread = new MonoClientThread(clientDataSocket, clientNotificationPort);
                executeIt.execute(monoClientThread);
                clientCount++;
            }
            catch (IOException e) {
                e.getMessage();//todo vlla это не обработка ошибки
            }
        }
        executeIt.shutdown();
    }

    private void start(int port, int nThreads) {
        try {
            executeIt = Executors.newFixedThreadPool(nThreads);
            clientCount = DEFAULT_CURRENT_CNT_CLIENTS;
            clientNotificationOutputStreams = new HashMap<>();
            clientDataOutputStreams = new HashMap<>();
            serverDataSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.getMessage();//todo vlla это не обработка ошибки
        }
    }

    public LinkedList<DataOutputStream> getClientNotificationOutputStreams() {
        return new LinkedList<>(clientNotificationOutputStreams.values()); //todo vlla чем вам коллекция то не угодила, зачем обязательно ее в List оборачивать? К тому же помним правило: отдаем коллекцию наружу - делаем ее unmodifiable
    }

    public LinkedList<DataOutputStream> getClientDataOutputStreams() {
        return new LinkedList<>(clientDataOutputStreams.values());
    }

    public void removeNotificationOutputStream(Integer key) {
        clientNotificationOutputStreams.remove(key);
    }

    public void addNotificationOutputStream(Integer key, DataOutputStream dataOutputStream) {
        clientNotificationOutputStreams.put(key, dataOutputStream);
    }

    public void addClientDataOutputStreams(Integer key, DataOutputStream dataOutputStream) {
        clientDataOutputStreams.put(key, dataOutputStream);
    }

    public void removeClientDataOutputStreams(Integer key) {
        clientDataOutputStreams.remove(key);
        --clientCount;
    }

    public DataOutputStream getDataOutputStream(int key) {
        return clientDataOutputStreams.get(key);
    }
}
