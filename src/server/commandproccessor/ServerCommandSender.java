package server.commandproccessor;

import constants.ConstantsClass;
import server.factories.ServerCommandFactory;
import server.model.Journal;
import server.model.Task;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

/**
 * Sends a command to the client side
 */

public class ServerCommandSender {
    private static ServerCommandSender instance;

    private ServerCommandSender() {
    }

    public static ServerCommandSender getInstance() { //todo vlla synchronized
        if (instance == null) instance = new ServerCommandSender();
        return instance;
    }

    /**
     * Sends an update command with a journal from the controller
     * @param journal
     * @param out data stream on which command will be transferred
     */

    public void sendUpdateCommand(Journal journal, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UPDATE, journal), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
            //todo vlla это уже гораздо лучше в плане обработки ошибки.
            //но есть несколько замечаний
            //во первых, и в главных : какая ответсвенность у ServerCommandSender? Отправлять команды в поток команд. Все. Почему он у вас занимается показом сообщений пользователю?
            //Может, тот, кто его использует, хочет по другому обработать ошибку? Почему этот класс берет на себя такую ответственность?
            //Может у вас вообще ваш слой контроллера прикручен не к графическому, а к текстровому интерфейсу и в этом контексте показ ОКНА пользоваетлю вообще не имеет смысла?
            //во вторых - у вас этот блок кода используется в 86 мест в программе, с различесем только в error message.
            //Напрашивается логичный шаг - вынести его в отдельный класс и в нужном месте просто вызывать метод, там showError("Some message"). Улучшает читаемость кода.

            //в общем, служебные классы, которые выполняют вполне конкретную функцию (в данном случае - отправка команды в поток) должны выполнять ТОЛЬКО эту функцию.
            //если эту функцию выполнить невозможно - они должны выбрасывать exception. Причем крайне желательно не стандартные ява-эксепшены, а ваши собственные, имеющие определенный смысл в вашем приложении
            //использующие их классы должны ловить этот эксепшен и, если они ответственные за его обработку - обрабатывать его, если не они - пробрасывать выше.
            //это базовый принцип работы с исключениями в яве, вам необходимо его освоить. Посмотрите весь свой код и проведите его глубокий рефакторинг.
            //Задавайте себе следующие вопросы:
            //Какая ответсвенность у этого класса? (это кстати должно быть описано в javadoc)
            //Не делает ли он что-то, что выходит за рамки его ответсвенности?
            //Какие исключительные ситуации возможны в каждом конкретном случае?
            //Входит ли в зону ответсвенности этого класса обработка этих исключений?
        }

    }

    /**
     * Sends a notification command with task which notification time has come
     * @param task
     * @param out data stream on which command will be transferred
     */

    public void sendNotificationCommand(Task task, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.NOTIFICATION, task), out);
            out.flush();
            //todo vlla шесть ваших методов отличаются ТОЛЬКО параметрами у createCommand().
            //напрашивается рефакторинг - завести служебный private метод, который будет делать все необходимые подготовительные и завершающие действия
            //а public методы будут лишь вызывать его с нужными параметрами
            //объем кода этого класса сократится в 6 раз
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends a command of a successful authorization of a user with a journal from the controller
     * @param journal
     * @param out data stream on which command will be transferred
     */

    public void sendSuccessfulAuthCommand(Journal journal, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.SUCCESSFUL_AUTH, journal), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends a command of an unsuccessful authorization of a user
     * @param out data stream on which command will be transferred
     */

    public void sendUnsuccessfulAuthCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UNSUCCESSFUL_SIGN_IN, new User()), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

     /**
     * Sends a command of an unsuccessful sign up of a user
     * @param out data stream on which command will be transferred
     */

    public void sendUnsuccessfulSignUpCommand(OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UNSUCCESSFUL_SIGN_UP, new User()),
                    out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends a command of an unsuccessful action requested by user
     * @param out data stream on which command will be transferred
     */

    public void sendUnsuccessfulActionCommand(String string, OutputStream out) {
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(ServerCommandFactory.createCommand(ConstantsClass.UNSUCCESSFUL_ACTION, string), out);
            out.flush();
        } catch (JAXBException | IOException e) {
            JOptionPane.showMessageDialog(null, "Could not send this command!",
                    "Incorrect command!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
