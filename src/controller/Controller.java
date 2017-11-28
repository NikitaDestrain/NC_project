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
    private static TaskStatus DEFAULT_STATUS = TaskStatus.Planned;
    private static Controller instance;

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
        this.idGenerator = new IDGenerator(journal.getMaxId());
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
    public void addTask(String name, String description, int day, int month, int year, int hour, int minute, int cntHoursForNotification, int cntMinutesForNotification) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        Date d1 = calendar.getTime(); //plannedDate
        calendar.set(year, month, day, hour - cntHoursForNotification, minute - cntMinutesForNotification);
        Date d2 = calendar.getTime(); //notifDate
        if(checkDate(d2)) {
            Task task = new Task(name, DEFAULT_STATUS, description, d2, d1);
            task.setId(idGenerator.createId());
            journal.addTask(task);
            notifier.addNotification(task);
        }
        else
            System.out.println("Invalid Date!!!"); //сообщение в форму о неверной дате
    }

    //удаляется таск(????) и оповещение
    public void removeTask(int id){
        notifier.cancelNotification(journal.getTask(id).getId());
        journal.removeTask(id);
    }

    //отменяется оповещение
    public void cancelNotification(int id){
        notifier.cancelNotification(journal.getTask(id).getId());
        journal.getTask(id).setStatus(TaskStatus.Cancelled);
    }
    //todo map create
    //изменяется оповещение (читать в классе Notifier описание по работе с методом)
    public void updateNotification(int id){
        Task task = journal.getTask(id);
        task.setStatus(TaskStatus.Rescheduled);
        notifier.editNotification(journal.getTask(id).getId(), task);
        journal.getTask(id).setStatus(TaskStatus.Rescheduled);
    }

    //изменение таски полностью !!!edit rename
    public void setTask(int id, String name, String description, int day, int month, int year, int hour, int minute, int cntHoursForNotification, int cntMinutesForNotification){
        //notifier.cancelNotification(journal.getTask(id).getId());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        Date d1 = calendar.getTime(); //plannedDate
        calendar.set(year, month, day, hour - cntHoursForNotification, minute - cntMinutesForNotification);
        Date d2 = calendar.getTime(); //notifDate
        if(checkDate(d2)) {
            Task task = new Task(name, DEFAULT_STATUS, description, d2, d1);//todo do not create
            notifier.cancelNotification(journal.getTask(id).getId());
            task.setId(idGenerator.createId());
            journal.setTask(id, task);
            notifier.addNotification(task);
        }
        else
            System.out.println("Invalid Date!!!"); //сообщение в форму о неверной дате

    }

    //probably we will use it later...
/*
    public Date parseStringToDate(String date) {
        //todo private. Or better - move it to Utils class
        Date parsedDate = null;
        try {
            parsedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            System.out.println("Некорректная дата"); // тут отправка сообщения в форму
        }
        return parsedDate;
    }

    private boolean isTwoDatesNotNull(Date date1, Date date2) {
        //todo --//--
        return date1 != null && date2 != null;
    }

    public void setTask(int id, String name, TaskStatus status, String description, String notificationDate, String plannedDate) {
        Date notification = parseStringToDate(notificationDate);
        Date planned = parseStringToDate(plannedDate);
        if (isTwoDatesNotNull(notification, planned)) {
            journal.setTask(id, new Task(name, status, description, notification, planned));
            //notifier edit
        }
        else
            System.out.println("Дата оповещения позже запланированной");
        //todo else?..
    }



 */
}
