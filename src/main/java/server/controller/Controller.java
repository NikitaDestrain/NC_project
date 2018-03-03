package server.controller;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.MessageBox;
import server.commandproccessor.ServerCommandSender;
import server.exceptions.IncorrectTaskStatusConversionException;
import server.exceptions.UnsuccessfulCommandActionException;
import server.gui.ServerTreatmentDetector;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;
import server.network.StreamContainer;
import server.properties.ParserProperties;

import javax.swing.*;
import java.io.DataOutputStream;
import java.util.*;

public class Controller {
    private Journal journal;
    private Notifier notifier;
    private static Controller instance;
    //private MainForm mainForm = MainForm.getInstance();
    private XMLSerializer serializer;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();
    private StreamContainer streamContainer = StreamContainer.getInstance();
    private MessageBox messageBox = MessageBox.getInstance();
    private LifecycleManager manager = LifecycleManager.getInstance();
    private ServerTreatmentDetector detector = ServerTreatmentDetector.getInstance();

    private Controller() {
        this.journal = new Journal();
        this.notifier = new Notifier();
        this.serializer = new XMLSerializer();
        try {
            setJournal(serializer.readJournal(ParserProperties.getInstance().getProperty(ConstantsClass.XML_FILE)));
        } catch (Exception e) {
            int action = JOptionPane.showConfirmDialog(null,
                    "Could not load journal from file!\nDo you want to create a new one?\n" +
                            "If you choose NO, the program execution will be stopped!",
                    "Error", JOptionPane.YES_NO_OPTION);

            if (action == JOptionPane.YES_OPTION) {
                setJournal(new Journal());
            } else {
                System.exit(1);
            }
        }
    }

    /**
     * Gets the instance of server.exceptions.controller using private constructor.
     *
     * @return If current <code>instance</code> is null, creates and returns new object,
     * otherwise returns current instance
     */

    public static synchronized Controller getInstance() {
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

    private boolean checkDate(Date date) {
        return !date.before(Calendar.getInstance().getTime());
    }

    /**
     * Sets the <code>Journal</code> of current server.exceptions.controller. Checks dates of tasks from received journal:
     * if notification date is overdue, sets task status <code>Overdue</code>,
     * otherwise task is added without any changes
     *
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
        List<DataOutputStream> streams = streamContainer.getClientNotificationOutputStreams();
        if (streams != null) {
            for (DataOutputStream out : streams)
                try {
                    commandSender.sendUpdateCommand(this.journal, out);
                } catch (UnsuccessfulCommandActionException e) {
                    messageBox.showMessage("Could not send Update command!");
                }
        }
    }

    private void sendUnsuccessfulCommand() {
        List<DataOutputStream> streams = streamContainer.getClientDataOutputStreams();
        if (streams != null) {
            for (DataOutputStream out : streams)
                try {
                    commandSender.sendUnsuccessfulActionCommand("Error! Incorrect action with task status!", out);
                } catch (UnsuccessfulCommandActionException e) {
                    messageBox.showMessage("Could not send Unsuccessful command!");
                }
        }
    }

    public void updateMainForm() {
        /*mainForm = MainForm.getInstance();
        if (mainForm != null)
            mainForm.updateJournal();*/
    }

    /**
     * Adds the received task to current journal, sets a notification for it and updates a table in <code>MainForm</code>
     *
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
     *
     * @param tasksNums string with id of tasks to be removed splitted with ','
     */

    public void removeTask(String tasksNums) {
        String[] tasks = tasksNums.split(",");
        for (int i = 0; i < tasks.length; i++) {
            notifier.cancelNotification(Integer.parseInt(tasks[i]));
            journal.removeTask(Integer.parseInt(tasks[i]));
        }
        updateMainForm();
        sendUpdateCommand();
    }

    private void processException() {
        if (detector.getDetector() == ConstantsClass.SERVER_TREATMENT) {
            detector.clearTreatment();
            throw new IncorrectTaskStatusConversionException();
        } else if (detector.getDetector() == ConstantsClass.NOT_SERVER_TREATMENT)
            sendUnsuccessfulCommand();
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Cancelled</code> status
     * and updates a <code>MainForm</code>
     *
     * @param id of task for which a notification is being cancelled
     */

    public void cancelNotification(int id) {
        Task task = journal.getTask(id);
        if (manager.isStatusConversionValid(task.getStatus(), TaskStatus.Cancelled)) {
            notifier.cancelNotification(id);
            journal.getTask(id).setStatus(TaskStatus.Cancelled);
            updateMainForm();
            sendUpdateCommand();
        } else {
            processException();
        }
    }

    /**
     * Cancels a notification for task in current journal, sets it a <code>Completed</code> status
     * and updates a <code>MainForm</code>
     *
     * @param id of completed task
     */

    public void finishNotification(int id) {
        if (manager.isStatusConversionValid(journal.getTask(id).getStatus(), TaskStatus.Completed)) {
            notifier.cancelNotification(id);
            journal.getTask(id).setStatus(TaskStatus.Completed);
            updateMainForm();
            sendUpdateCommand();
        } else {
            processException();
        }
    }

    /**
     * Updates a notification for the task in the current <code>Journal</code>
     *
     * @param task task to be updated
     * @see Notifier#editNotification(Task)
     */

    public void updateNotification(Task task) {
        if (manager.isStatusConversionValid(task.getStatus(), TaskStatus.Rescheduled)) {
            journal.removeTask(task.getId());
            task.setStatus(TaskStatus.Rescheduled);
            journal.addTask(task);
            notifier.editNotification(task);
            updateMainForm();
            sendUpdateCommand();
        } else {
            processException();
        }
    }

    /**
     * Edits the received task
     *
     * @param task to be edited
     * @see Notifier#editNotification(Task)
     */

    public void editTask(Task task) {
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
