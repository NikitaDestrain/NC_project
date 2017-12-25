package client.network;

import client.gui.authforms.AuthForm;

public class StartClient {

    public static void main(String[] args) {


        new AuthForm().setVisible(true);
        //ClientNetworkFacade cnf = ClientNetworkFacade.getInstance();
        //cnf.start();
    }
}
