package client.network;

import client.gui.AuthForm;

public class StartClient {

    public static void main(String[] args) {
      //  new AuthForm().setVisible(true);
       ClientNetworkFacade cnf = new ClientNetworkFacade();
        cnf.start();
    }
}
