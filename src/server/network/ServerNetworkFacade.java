package server.network;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.MessageBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade for managing client's threads
 */

public class ServerNetworkFacade extends Thread {
    private int serverPort;
    private ServerSocket serverDataSocket;
    private Socket clientDataSocket;
    private ExecutorService executeIt;
    private StreamContainer streamContainer = StreamContainer.getInstance();
    private Map<Integer, MonoClientThread> clients;
    private static ServerNetworkFacade instance;
    private MessageBox messageBox = MessageBox.getInstance();
    private int clientCount;

    private ServerNetworkFacade() {}

    public static ServerNetworkFacade getInstance() {
        //todo vlla - тред-синглетон, это что-то странное. Давайте мы этот класс распилим на два? 1/2 DONE
        // Первый - который хранит в себе все мапы потоков и производит с ними махинации DONE
        // Второй - просто тред, который просто будет ждать подключения новых клиентов. DONE
        // Это все же разные обязанности.
        if (instance == null) instance = new ServerNetworkFacade();
        return instance;
    }

    @Override
    public void run() {
        System.out.println("Server logs:");
        serverPort = ConstantsClass.DEFAULT_SERVER_PORT;
        start(serverPort, ConstantsClass.DEFAULT_MAX_COUNT_CLIENTS);
        while (!serverDataSocket.isClosed()) {
            try {
                clientDataSocket = serverDataSocket.accept();
                int clientNotificationPort = PortGenerator.getInstance().createPort();
                MonoClientThread monoClientThread = new MonoClientThread(clientDataSocket, clientNotificationPort);
                executeIt.execute(monoClientThread);
                clients.put(clientNotificationPort, monoClientThread);
                clientCount++;
            }
            catch (IOException e) {
                messageBox.showMessage(ConstantsClass.ERROR_CLIENT_CONNECTION);//todo vlla поздравляю, вы выиграли приз за самую ужасную обработку исключительной ситуации в истории явы ) DONE
            }
        }
    }

    private void start(int port, int nThreads) {
        try {
            executeIt = Executors.newFixedThreadPool(nThreads);
            clientCount = ConstantsClass.DEFAULT_CURRENT_COUNT_CLIENTS;
            clients = new HashMap<>();
            serverDataSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            messageBox.showMessage(ConstantsClass.ERROR_SERVER_START);
        }
    }

    /**
     * Finishes server facade and closes socket
     */

    public void finish() {
        try {
            serverDataSocket.close();
            executeIt.shutdown();
            System.out.println("Correct finish");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes client's information from map
     * @param port
     */

    public void finishClient(int port) {
        clients.get(port).finish();
        clients.remove(port);
        --clientCount;
    }
}
