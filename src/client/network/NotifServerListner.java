package client.network;

import client.commandprocessor.InputCommandHandler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class NotifServerListner extends Thread {

    private DataInputStream dataInputstream;

    public NotifServerListner(DataInputStream in)
    {
        this.dataInputstream =in;
    }


    public void run(){
        System.out.println("\nЗапущен NotifServerListner\n");
        BufferedReader buffInput = new BufferedReader(new InputStreamReader(dataInputstream));

        while (true)
        {
            //парсинг

            //вызов класса который это команду обрабатывает
           // InputCommandHandler.readInputCommand(command);
        }
        }


    }

