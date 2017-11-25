package GUI;

import GUI.MainForm.MainForm;
import GUI.NotificationWindow.NotificationForm;
import GUI.TaskWindow.TaskForm;
import GUI.TaskWindow.TaskWindow;
import Properties.Prop;
import controller.SerializeDeserialize;
import model.Journal;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {

        try
        {
            Prop prop = new Prop(); //поля этого объекта содержат необхдимые значения



        } catch (IOException e)

        {
            System.out.println(e.toString()+" не верно указан файл конфигурации");;
        }

        // todo свернуть в трей
        // todo иниц файл, иниц генератор на журнал
       /*try {
            Journal journal = new SerializeDeserialize().readJournal();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        SwingUtilities.invokeLater(() -> new MainForm().setJournal(null));

      //  TaskWindow window = new TaskWindow();

        TaskForm  e = new TaskForm();
        e.setVisible(true);


    }
}
