package client.commandprocessor;


import javax.swing.*;

public class InputCommandHandler
{
    //читает пришедшую команду и вызывает необходимые методы контроллера

    public synchronized static void readInputCommand(Command inputCommand)
    {
        switch (inputCommand.getName())
        {
            case "Unsuccessful auth":
                JOptionPane.showMessageDialog( null,"Unsuccessful auth!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case "Successful auth":
                JOptionPane.showMessageDialog( null,"Successful auth!", "Connect", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog( null,"Тут вызов окна с журналом и отрисовка и тд", "Журнал", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Update":
               //uPdate журнала
                break;
            case "Notification":
                //уведомление о задаче
                break;
        }

    }
}
