package client.network;


import client.commandprocessor.InputCommandHandler;

import java.io.*;

//клас описывает  прослушивающий поток в который сервер кидает служебную информацию (например прошел атенфикацию или нет)
public class DataServerListner extends Thread
{

    private DataInputStream dataInputstream;

    public DataServerListner(DataInputStream in)
    {
        this.dataInputstream =in;
    }


    public void run(){
        System.out.println("\nЗапущен DataServerListner\n");
        BufferedReader buffInput = new BufferedReader(new InputStreamReader(dataInputstream));

        while(true)
        {
           // парсинг пришедшей команды


            //вызов класса который это команду обрабатывает
           // InputCommandHandler.readInputCommand(command);
        }


    }

}
