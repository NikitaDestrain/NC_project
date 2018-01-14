package server.network;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.MessageBox;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade for managing client's threads and their channels
 */

public class ServerNetworkFacade extends Thread {
    private int serverPort;
    private ServerSocket serverDataSocket;
    private Socket clientDataSocket;
    private ExecutorService executeIt;
    private Map<Integer, DataOutputStream> clientNotificationOutputStreams;
    private Map<Integer, DataOutputStream> clientDataOutputStreams;
    private Map<Integer, MonoClientThread> clients;
    private static ServerNetworkFacade instance;
    private MessageBox messageBox = MessageBox.getInstance();
    private int clientCount;

    private ServerNetworkFacade() {}

    public static ServerNetworkFacade getInstance() {
        //todo vlla - тред-синглетон, это что-то странное. Давайте мы этот класс распилим на два?
        // Первый - который хранит в себе все мапы потоков и производит с ними махинации
        // Второй - просто тред, который просто будет ждать подключения новых клиентов.
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
        executeIt.shutdown();
    }

    private void start(int port, int nThreads) {
        try {
            executeIt = Executors.newFixedThreadPool(nThreads);
            clientCount = ConstantsClass.DEFAULT_CURRENT_COUNT_CLIENTS;
            clientNotificationOutputStreams = new HashMap<>();
            clientDataOutputStreams = new HashMap<>();
            clients = new HashMap<>();
            serverDataSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            messageBox.showMessage(ConstantsClass.ERROR_SERVER_START);
        }
    }

    /**
     * Returns list of actual channels for notification to clients
     * @return LinkedList
     */

    public List<DataOutputStream> getClientNotificationOutputStreams() {
        LinkedList <DataOutputStream> list = new LinkedList<>(clientNotificationOutputStreams.values());
        return Collections.unmodifiableList(list);
        //todo vlla чем вам коллекция то не угодила, зачем обязательно ее в List оборачивать? К тому же помним правило: отдаем коллекцию наружу - делаем ее unmodifiable
        //!!!Не делается unmodified, преобразуется в лист для более быстрой итерации, так как ключи уже не будут играть роли
        //todo vlla 2 unmodified сделать все таки нужно. Выигрыш, получаемый от более быстрой итерации нивелируется накладными расходами по клонированию коллекции. DONE
        // Ключи и не нужны - clientNotificationOutputStreams.values() уже возвращает только коллекцию значений
    }

    /**
     * Returns list of actual channels for communicate to clients
     * @return LinkedList
     */

    public List<DataOutputStream> getClientDataOutputStreams() {
        LinkedList <DataOutputStream> list = new LinkedList<>(clientDataOutputStreams.values());
        return Collections.unmodifiableList(list);
    }

    protected void removeNotificationOutputStream(Integer key) {
        clientNotificationOutputStreams.remove(key);
        //todo vlla просто удалить стрим из мапы - не достаточно. Стримы всегда надо закрывать. DONE
        // закрыты в фасаде
    }

    protected void addNotificationOutputStream(Integer key, DataOutputStream dataOutputStream) {
        clientNotificationOutputStreams.put(key, dataOutputStream);
    }

    protected void addClientDataOutputStreams(Integer key, DataOutputStream dataOutputStream) {
        clientDataOutputStreams.put(key, dataOutputStream);
    }

    protected void removeClientDataOutputStreams(Integer key) {
        clientDataOutputStreams.remove(key);
        --clientCount;
    }

    public DataOutputStream getDataOutputStream(int key) {
        return clientDataOutputStreams.get(key);
    }

    /**
     * Deletes client's information from map
     * @param port
     */

    public void finishClient(int port) {
        clients.get(port).finish();
        clients.remove(port);
    }

}
