package server.network;

public class StartServer {

    public static void main(String[] args) {
        ServerNetworkFacade snf = ServerNetworkFacade.getInstance();
        snf.start();
    }
}
