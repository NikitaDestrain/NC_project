package server.controller;

import server.commandproccessor.User;
import server.gui.mainform.MainForm;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Journal journal;
    private Notifier notifier;
    private static Controller instance;
    private MainForm mainForm = MainForm.getInstance();
    private Map<String, String> usersAuthData;

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
        this.usersAuthData = new HashMap<>();
    }

    /**
     * Gets the instance of server.exceptions.controller using private constructor.
     * @return If current <code>instance</code> is null, creates and returns new object,
     * otherwise returns current instance
     */

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    /**
     * Gets the <code>Journal</code> object of current server.exceptions.controller
     */

    public Journal getJournal() {
        return journal;
    }

    private boolean checkDate(Date date){
        return !date.before(Calendar.getInstance().getTime());
    }

    /**
     * Sets the <code>Journal</code> of current server.exceptions.controller. Checks dates of tasks from received journal:
     * if notification date is overdue, sets task status <code>Overdue</code>,
     * otherwise task is added without any changes
     * @param journal with tasks
     */

    public void setJournal(Journal journal) {
        this.journal = journal;
        for (Task task : journal.getTasks()) {
            if (checkDate(task.getNotificationDate()) && (task.getStatus() == TaskStatus.Planned || task.getStatus() == TaskStatus.Rescheduled)) {
                notifier.addNotification(task);
            } else {
                if (task.getStatus() == TaskStatus.Planned || task.getStatus() == TaskStatus.Rescheduled)
                    task.setStatus(TaskStatus.Overdue);
            }
        }
        mainForm.updateJournal();
    }

    /**
     * Adds the received task to current journal, sets a notification for it and updates a table in <code>MainForm</code>
     * @param task to be added
     */

    public void addTask(Task task) {
        journal.addTask(task);
        notifier.addNotification(task);
        mainForm.updateJournal();
    }

    /**
     * Removes a task from current journal, cancels a notification for it and updates a table in <code>MainForm</code>
     * @param id of task to be removed
     */

    public void removeTask(int id){
        notifier.cancelNotification(id);
        journal.removeTask(id);
        mainForm.updateJournal();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Cancelled</code> status
     * and updates a <code>MainForm</code>
     * @param id of task for which a notification is being cancelled
     */

    public void cancelNotification(int id){
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Cancelled);
        mainForm.updateJournal();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Completed</code> status
     * and updates a <code>MainForm</code>
     * @param id of completed task
     */

    public void finishNotification(int id) {
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Completed);
        mainForm.updateJournal();
    }

    /**
     * Updates a notification for the task in the current <code>Journal</code>
     * @param id of task to be updated
     * @see Notifier#editNotification(Task)
     */

    public void updateNotification(int id){
        Task task = journal.getTask(id);
        task.setStatus(TaskStatus.Rescheduled);
        notifier.editNotification(task);
        mainForm.updateJournal();
    }

    /**
     * Edits the received task
     * @param task to be edited
     * @see Notifier#editNotification(Task)
     */

    public void editTask(Task task){
        notifier.editNotification(task);
        mainForm.updateJournal();
    }

    /**
     * Checks if user with current login exists in user's map and its password equals password from parameter
     */

    public boolean isUserDataCorrect(User user) {
        if (user == null) return false;
        return usersAuthData.containsKey(user.getLogin()) && usersAuthData.get(user.getLogin()).equals(user.getPassword());
    }

    public boolean isSuchLoginExists(String login) {
        return usersAuthData.containsKey(login);
    }

    public void addUser(User user) {
        if (user != null) {
            usersAuthData.put(user.getLogin(), user.getPassword());
        }
    }
}
