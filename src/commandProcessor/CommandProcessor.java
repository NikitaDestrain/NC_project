package commandProcessor;

import server.factories.CommandFactory;
import server.model.Journal;
import server.model.Task;

public class CommandProcessor {
    //создание
    public static Command createAddCommand(Task task) {
        return CommandFactory.createCommand("Add", task);
    }

    //изменение
    public static Command createEditCommand(Task task) {
        return CommandFactory.createCommand("Edit", task);
    }

    //удаление
    public static Command createDeleteCommand(Task task) {
        return CommandFactory.createCommand("Delete", task);
    }

    //апдейт журнала
    public static Command createUpdateCommand(Journal journal) {
        return CommandFactory.createCommand("Update", journal);
    }

    //нотификация клиенту
    public static Command createNotificationCommand(Task task) {
        return CommandFactory.createCommand("Notification", task);
    }

    //отложить нотификацию(идут серверу)
    public static Command createLaterCommand(Task task) {
        return CommandFactory.createCommand("Later", task);
    }

    //завершить
    public static Command createFinishCommand(Task task) {
        return CommandFactory.createCommand("Finish", task);
    }

    //отменить(ставится статус отменена)
    public static Command createCancelCommand(Task task) {
        return CommandFactory.createCommand("Cancel", task);
    }
}
