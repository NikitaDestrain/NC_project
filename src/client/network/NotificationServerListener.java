package client.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

public class NotificationServerListener extends Thread {

    private DataInputStream dataInputstream;

    public NotificationServerListener(DataInputStream in)
    {
        this.dataInputstream = in;
    }

    public void run() {
        System.out.println("\nЗапущен NotificationServerListener\n");
        BufferedReader buffInput = new BufferedReader(new InputStreamReader(dataInputstream));
        while (true) {
            //парсинг
            //вызов класса который это команду обрабатывает
           // InputCommandHandler.readInputCommand(command);
        }
    }
}

