package client;

import client.commandprocessor.ClientCommandProcessor;
import client.commandprocessor.Command;
import client.factories.ClientCommandFactory;
import server.commandproccessor.ParserCommand;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String[] args) {
        String pass = null;
        try {
            pass = new PasswordEncoder().encode("1234");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //ClientCommandFactory.createAuthCommand("auth", "artem", pass);
        try {
            ClientCommandProcessor.sendAuthCommand("auth", "artem", pass, // или auth или signUp
                    new FileOutputStream("src/client/test.xml"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            server.commandproccessor.AuthCommand command = ParserCommand.parseAuthCommand(
                    new FileInputStream("src/client/test.xml"));
            System.out.println(command.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
