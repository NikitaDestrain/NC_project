package client.network;

public class StartClient {

    public static void main(String[] args) {
        ClientNetworkFacade cnf = new ClientNetworkFacade();
        cnf.start();
    }
}
