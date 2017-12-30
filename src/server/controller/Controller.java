package server.controller;

import constants.ConstantsClass;
import server.commandproccessor.ServerCommandSender;
import server.gui.mainform.MainForm;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;
import server.network.ServerNetworkFacade;
import server.properties.ParserProperties;

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
            setJournal(serializer.readJournal(ParserProperties.getInstance().getProperties(ConstantsClass.XML_FILE)));
        } catch (Exception e) {
            if (JOptionPane.showConfirmDialog(null,
                    "Could not load journal from file!\nDo you want to create a new one?\n" +
                            "If you choose NO, the program execution will be stopped!",
                    "Error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                setJournal(new Journal());
                //todo vlla очень плохо читается блок кода. Стоит завести переменную, сохранить в нее рузультат диалогового окна и в if просто сравнить ее с константой
            }
            else System.exit(1); //todo vlla использование System.exit - это дурной тон. В реальном приложении вы никогда не будуте его использовать.
            //Приложение нужно завершать корректно: стримы, файлы и сокеты - закрывать, потоки - интерраптить и завершать.
            //Нужно будет произвести глобальный рефакторинг - обеспечить корретное завершение с освобождение всех ресурсов.
        }
    }

    /**
     * Gets the instance of server.exceptions.controller using private constructor.
     * @return If current <code>instance</code> is null, creates and returns new object,
     * otherwise returns current instance
     */

    public static Controller getInstance() { //todo vlla потенциальные проблемы с синхронизацией
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
            //todo vlla а где собственно происходит проверка, что смена стратуса - валидна? На сколько я помню, мы договорились, что статус таски должен меняться усключительно согласно графу переходов
            // а у вас в куче мест кода просто вызывается setStatus, без какие либо проверок. Подключаем сюда несколько пользователей и гарантированно ловим ситуацию, когда статусы будут менять в обход графа переходов.
            // часть со статусами надо серьезно доделать: выделить сущность, когда будет ответственной только за контроль смены статусов тасок (это может быть контроллер, но я советую завести какой нибудь LifecycleManager)
            // в этом классе реализуем корректный перевод таски из одного статуса в друой: если переход разрешен согласно графу переходов - меняем статус, если запрещем - выдаем специальный эксепшен и корректно обрабатываем его выше.
            // причем вы можете непосредственный вызов этого LifecycleManager сделать в самом методе setStatus, чтобы существенно не менять всю ту часть когда, который setStatus использует.
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
