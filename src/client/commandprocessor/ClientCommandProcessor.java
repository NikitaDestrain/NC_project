package client.commandprocessor;

import client.factories.ClientCommandFactory;
import client.model.Task;

public class ClientCommandProcessor {
    //создание
    public static Command createAddCommand(Task task) { return ClientCommandFactory.createCommand("Add", task); }

    //изменение
    public static Command createEditCommand(Task task) {
        return ClientCommandFactory.createCommand("Edit", task);
    }

    //удаление
    public static Command createDeleteCommand(Task task) {
        return ClientCommandFactory.createCommand("Delete", task);
    }

    //отложить нотификацию(идут серверу)
    public static Command createLaterCommand(Task task) {
        return ClientCommandFactory.createCommand("Later", task);
    }

    //завершить
    public static Command createFinishCommand(Task task) {
        return ClientCommandFactory.createCommand("Finish", task);
    }

    //отменить(ставится статус отменена)
    public static Command createCancelCommand(Task task) {
        return ClientCommandFactory.createCommand("Cancel", task);
    }

    public static Command createSignInCommand(String login, String password) {
        return ClientCommandFactory.createCommand("Sign in", new User(login, password));
    }

    public static Command createSignUpCommand(String login, String password) {
        return ClientCommandFactory.createCommand("Sign up", new User(login, password));
    }
}
