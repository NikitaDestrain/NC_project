package server.controller;

import server.commandproccessor.ServerCommandSender;
import server.gui.mainform.MainForm;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;
import server.network.ServerNetworkFacade;

import javax.swing.*;
import java.io.DataOutputStream;
import java.util.*;

public class Controller {
    private Journal journal;
    private Notifier notifier;
    private static Controller instance;
    private MainForm mainForm = MainForm.getInstance();
    private XMLSerializer serializer;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();
    private ServerNetworkFacade facade = ServerNetworkFacade.getInstance();

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
        this.serializer = new XMLSerializer();
        try {
            setJournal(serializer.readJournal(ParserProperties.getInstance().getProperties(PropertiesConstant.XML_FILE.toString())));
        } catch (Exception e) {
            if (JOptionPane.showConfirmDialog(null,
                    "Could not load journal from file!\nDo you want to create a new one?\n" +
                            "If you choose NO, the program execution will be stopped!",
                    "Error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                setJournal(new Journal());
            }
            else System.exit(1);
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
        LinkedList<DataOutputStream> streams = facade.getClientDataOutputStreams();
        if(streams != null) {
            for (DataOutputStream out : streams)
                commandSender.sendUpdateCommand(this.journal, out);
        }
    }

    private void sendUnsuccessfulCommand() {
        LinkedList<DataOutputStream> streams = facade.getClientDataOutputStreams();
        if(streams != null) {
            for (DataOutputStream out : streams)
                commandSender.sendUnsuccessfulActionCommand("Error! Incorrect action with task status!", out);
        }
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
     * @param tasksNums string with id of tasks to be removed splitted with ','
     */

    public void removeTask(String tasksNums){
        String[] tasks = tasksNums.split(",");
        for (int i = 0; i < tasks.length; i++) {
            notifier.cancelNotification(Integer.parseInt(tasks[i]));
            journal.removeTask(Integer.parseInt(tasks[i]));
        }
        updateMainForm();
        sendUpdateCommand();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Cancelled</code> status
     * and updates a <code>MainForm</code>
     * @param id of task for which a notification is being cancelled
     */

    public void cancelNotification(int id){
        Task task = journal.getTask(id);
        if (task.getStatus() != TaskStatus.Completed) {
            notifier.cancelNotification(id);
            journal.getTask(id).setStatus(TaskStatus.Cancelled);
            updateMainForm();
            sendUpdateCommand();
        }
        else sendUnsuccessfulCommand();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Completed</code> status
     * and updates a <code>MainForm</code>
     * @param id of completed task
     */

    public void finishNotification(int id) {
        if (journal.getTask(id).getStatus() != TaskStatus.Cancelled) {
            notifier.cancelNotification(id);
            journal.getTask(id).setStatus(TaskStatus.Completed);
            updateMainForm();
            sendUpdateCommand();
        }
        else sendUnsuccessfulCommand();
    }

    /**
     * Updates a notification for the task in the current <code>Journal</code>
     * @param task task to be updated
     * @see Notifier#editNotification(Task)
     */

    public void updateNotification(Task task) {
        if (task.getStatus() != TaskStatus.Completed && task.getStatus() != TaskStatus.Cancelled) {
            journal.removeTask(task.getId());
            task.setStatus(TaskStatus.Rescheduled);
            journal.addTask(task);
            notifier.editNotification(task);
            updateMainForm();
            sendUpdateCommand();
        }
        else sendUnsuccessfulCommand();
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

    public void setOverdue(int id) {
        journal.getTask(id).setStatus(TaskStatus.Overdue);
        updateMainForm();
    }
}
