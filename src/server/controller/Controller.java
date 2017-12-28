package server.controller;

import server.commandproccessor.ServerCommandSender;
import server.commandproccessor.User;
import server.gui.mainform.MainForm;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;
import server.network.ServerNetworkFacade;
import server.properties.ParserProperties;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Journal journal;
    private Notifier notifier;
    private static Controller instance;
    private MainForm mainForm = MainForm.getInstance();
    private Map<String, String> userData;
    private XMLSerializer serializer;
    private UserDataSerializer userDataSerializer;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();
    private ServerNetworkFacade facade = ServerNetworkFacade.getInstance();

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
        this.userDataSerializer = new UserDataSerializer();
        try {
            this.userData = userDataSerializer.readData(ParserProperties.getInstance()
                    .getProperties("USER_DATA"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load user data from file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.serializer = new XMLSerializer();
        try {
            setJournal(serializer.readJournal(ParserProperties.getInstance().getProperties("XML_FILE")));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not load journal from file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            setJournal(new Journal());
        }
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
    }

    private void sendUpdateCommand() {
        for (DataOutputStream out: facade.getClientDataOutputStreams())
            commandSender.sendUpdateCommand(this.journal, out);
    }

    public void updateMainForm() {
        mainForm = MainForm.getInstance();
        if (mainForm != null)
            mainForm.updateJournal();
    }

    /**
     * Adds the received task to current journal, sets a notification for it and updates a table in <code>MainForm</code>
     * @param task to be added
     */

    public void addTask(Task task) {
        journal.addTask(task);
        notifier.addNotification(task);
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Removes a task from current journal, cancels a notification for it and updates a table in <code>MainForm</code>
     * @param id of task to be removed
     */

    public void removeTask(int id){
        notifier.cancelNotification(id);
        journal.removeTask(id);
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Cancelled</code> status
     * and updates a <code>MainForm</code>
     * @param id of task for which a notification is being cancelled
     */

    public void cancelNotification(int id){
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Cancelled);
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Completed</code> status
     * and updates a <code>MainForm</code>
     * @param id of completed task
     */

    public void finishNotification(int id) {
        notifier.cancelNotification(id);
        journal.getTask(id).setStatus(TaskStatus.Completed);
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Updates a notification for the task in the current <code>Journal</code>
     * @param task task to be updated
     * @see Notifier#editNotification(Task)
     */

    public void updateNotification(Task task){
        journal.removeTask(task.getId());
        task.setStatus(TaskStatus.Rescheduled);
        journal.addTask(task);
        notifier.editNotification(task);
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Edits the received task
     * @param task to be edited
     * @see Notifier#editNotification(Task)
     */

    public void editTask(Task task){
        notifier.editNotification(task);
        journal.removeTask(task.getId());
        journal.addTask(task);
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Checks if user with current login exists in user's map and its password equals password from parameter
     */

    public boolean isUserDataCorrect(User user) {
        if (user == null) return false;
        return userData.containsKey(user.getLogin()) &&
                userData.get(user.getLogin()).equals(user.getPassword());
    }

    public boolean isSuchLoginExists(String login) {
        return userData.containsKey(login);
    }

    public void addUser(User user) {
        if (user != null) {
            userData.put(user.getLogin(), user.getPassword());
        }
    }

    public void writeUserData(String path) {
        try {
            userDataSerializer.writeData(this.userData, path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not write user data to file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
