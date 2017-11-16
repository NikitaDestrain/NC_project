package controller;

import model.Journal;
import model.Task;
import model.TaskStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Controller {

    private Journal journal;

    public Controller(Journal journal) {
        this.journal = journal;
    }

    public Date parseStringToDate(String date) {
        Date parsedDate = null;
        try {
            parsedDate = new SimpleDateFormat("dd-Mmm-yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            System.out.println("Некорректная дата"); // тут отправка сообщения в форму
        }
        return parsedDate;
    }

    private boolean isTwoDatesNotNull(Date date1, Date date2) {
        return date1 != null && date2 != null;
    }

    public void setTask(int id, String name, TaskStatus status, String description, String notificationDate, String plannedDate) {
        Date notification = parseStringToDate(notificationDate);
        Date planned = parseStringToDate(plannedDate);
        if (isTwoDatesNotNull(notification, planned))
        journal.setTask(id, new Task(name, status, description, notification, planned));
    }

    public void removeTask(int id) {
        journal.removeTask(id);
    }

    public void addTask(String name, TaskStatus status, String description, String notificationDate, String plannedDate) {
        Date notification = parseStringToDate(notificationDate);
        Date planned = parseStringToDate(plannedDate);
        if (isTwoDatesNotNull(notification, planned)) {
            if (notification.before(planned)) {
                journal.addTask(name, status, description, notification, planned);
            } else {
                System.out.println("Дата оповещения позже запланированной"); // тут отправка сообщения в форму
            }
        }
    }
}
