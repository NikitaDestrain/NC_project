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
        //Journal journal = null;
          //  journal = new SerializeDeserialize().readJournal();

        //MainForm form = new MainForm();
        TaskWindow window = new TaskWindow(new MainForm());
        window.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                try {
                    new MainForm().setJournal(new SerializeDeserialize().readJournal());
                }
                catch (IOException e) {}
            });




        //TaskForm  e = new TaskForm();
        //e.setVisible(true);


    }
}
