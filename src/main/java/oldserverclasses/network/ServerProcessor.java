package oldserverclasses.network;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.MessageBox;
import server.exceptions.IllegalPropertyException;
import oldserverclasses.gui.authforms.AuthForm;
import oldserverclasses.properties.ParserProperties;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerProcessor {

    private Map<Integer, MonoClientThread> clients;
    private ServerNetworkFacade serverNetworkFacade;
    private static ServerProcessor instance;

    private ServerProcessor() {
        clients = new HashMap<>();
        instance = this;
    }

    public static ServerProcessor getInstance() {
        if (instance == null) instance = new ServerProcessor();
        return instance;
    }

    public static void main(String[] args) {
        try {
            ParserProperties.getInstance().getProperty(ConstantsClass.MAIN_FORM_ICON);
        } catch (IllegalPropertyException | IOException e) {
            MessageBox.getInstance().showMessage(ConstantsClass.ERROR_PROPERTY);
            return;
        }
        getInstance();
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }

    public ServerNetworkFacade getServerNetworkFacade() {
        return serverNetworkFacade;
    }

    public void addClient(int clientPort, MonoClientThread clientThread) {
        clients.put(clientPort, clientThread);
    }

    /**
     * Deletes oldclientclasses.client's information from map and closes all channels
     *
     * @param clientPort
     */

    public void finishClient(int clientPort) {
        System.out.printf("Client with port %d disconnected.\n", clientPort);
        clients.get(clientPort).interrupt();
        clients.get(clientPort).finish();
        clients.remove(clientPort);
    }

    /**
     * Starts server facade
     */

    public void startServer() {
        serverNetworkFacade = new ServerNetworkFacade();
        if (!serverNetworkFacade.isAlive()) serverNetworkFacade.start();
    }

    /**
     * Finishes server facade
     */

    public void finishServer() {
        serverNetworkFacade.interrupt();
        serverNetworkFacade.finish();
        finishAllClients();
    }

    private void finishAllClients() {
        for (MonoClientThread client : clients.values()) {
            client.interrupt();
            client.finish();
        }
        System.out.println("All clients have been disconnected by server.");
    }

}
