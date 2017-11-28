package controller;

import model.Journal;
import model.Task;
import model.TaskStatus;
import java.util.Calendar;
import java.util.Date;

public class Controller {

    private Journal journal;
    private Notifier notifier;
    private IDGenerator idGenerator;
    private static Controller instance;

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
        this.idGenerator = IDGenerator.getInstance(journal.getMaxId());
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

    //создается таск вместе с оповещением
    public void addTask(Task task) {
        if(checkDate(task.getNotificationDate())) {
            journal.addTask(task);
            notifier.addNotification(task);
        }
        else
            System.out.println("Invalid Date!!!"); //сообщение в форму о неверной дате
    }

    //удаляется таск(????) и оповещение
    public void removeTask(int id){
        notifier.cancelNotification(id);
        journal.removeTask(id);
    }

    //отменяется оповещение
    public void cancelNotification(int id){
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Cancelled);
    }
    //todo map create
    //изменяется оповещение (читать в классе Notifier описание по работе с методом)
    public void updateNotification(int id){
        Task task = journal.getTask(id);
        task.setStatus(TaskStatus.Rescheduled);
        notifier.editNotification(id, task);
        journal.getTask(id).setStatus(TaskStatus.Rescheduled);
    }

    //изменение таски полностью !!!edit rename
    public void editTask(int id, Task task){
        journal.setTask(id, task);
        notifier.editNotification(id, task);
    }
}
