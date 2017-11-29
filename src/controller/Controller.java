package controller;

import gui.mainform.MainForm;
import model.Journal;
import model.Task;
import model.TaskStatus;
import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class Controller {

    private Journal journal;
    private Notifier notifier;
    private static Controller instance;
    private MainForm mainForm = MainForm.getInstance();

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
    }

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public Journal getJournal() {
        return journal;
    }

    private boolean checkDate(Date date){
        return !date.before(Calendar.getInstance().getTime());
    }

    public void addTaskFromBackup(Task task) {
        if(checkDate(task.getNotificationDate())) {
            notifier.addNotification(task);
        }
        else {
            if(task.getStatus() == TaskStatus.Planned || task.getStatus() == TaskStatus.Rescheduled)
                task.setStatus(TaskStatus.Overdue);
        }
        journal.addTask(task);
        mainForm.updateJournal();
    }

    //создается таск вместе с оповещением
    public void addTask(Task task) {
        if(checkDate(task.getNotificationDate())) {
            journal.addTask(task);
            notifier.addNotification(task);
            mainForm.updateJournal();
        }
        else
            JOptionPane.showMessageDialog(null, "Task you intended to add has incorrect notification time!",
                    "Error", JOptionPane.ERROR_MESSAGE);
    }

    //удаляется таск и оповещение
    public void removeTask(int id){
        notifier.cancelNotification(id);
        journal.removeTask(id);
        mainForm.updateJournal();
    }

    //отменяется оповещение
    public void cancelNotification(int id){
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Cancelled);
        mainForm.updateJournal();
    }

    public void finishNotification(int id) {
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Completed);
        mainForm.updateJournal();
    }

    //изменяется оповещение (читать в классе Notifier описание по работе с методом)
    public void updateNotification(int id){
        Task task = journal.getTask(id);
        task.setStatus(TaskStatus.Rescheduled);
        notifier.editNotification(id, task);
        journal.getTask(id).setStatus(TaskStatus.Rescheduled);
        mainForm.updateJournal();
    }

    //изменение таски полностью !!!edit rename
    public void editTask(int id, Task task){
        journal.setTask(id, task);
        notifier.editNotification(id, task);
        mainForm.updateJournal();
    }
}
