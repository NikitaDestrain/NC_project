
import GUI.MainForm.MainForm;
import GUI.NotificationWindow.NotificationForm;
import GUI.TaskWindow.TaskWindow;
import Properties.Prop;
import controller.Controller;
import controller.SerializeDeserialize;
import model.Journal;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args)
    {
//        Controller controller = Controller.getInstance();
//        controller.addTask("Test", "Test", 27,10,2017,16,17, 0,0);
//        controller.addTask("Test", "Test", 27,10,2017,23,35, 0,0);
//        controller.addTask("Test", "Test", 27,10,2017,23,36, 0,0);
//        controller.addTask("Test", "Test", 27,10,2017,23,37, 0,0);
//        controller.removeTask(2);
//        controller.addTask("Test", "Test", 28,10,2017,23,37, 0,0);
        //controller.editNotification(0);
        //controller.addTask("Test", "Test", 26,11,2017,22,30, 0,5);
        //controller.addTask("Test", "Test", 26,11,2017,22,30, 0,5);
        //SwingUtilities.invokeLater(() -> new MainForm().setJournal(controller.getJournal()));

//        try
//        {
//            Prop prop = new Prop(); //поля этого объекта содержат необхдимые значения
//        } catch (IOException e)
//        {
//            System.out.println(e.toString()+" не верно указан файл конфигурации");;
//        }

        // todo свернуть в трей
        // todo иниц файл, иниц генератор на журнал
       /*try {
            Journal journal = new SerializeDeserialize().readJournal();
        } catch (IOException e) {
            e.printStackTrace();
        }*/




      //  TaskWindow window = new TaskWindow();


    }
}
