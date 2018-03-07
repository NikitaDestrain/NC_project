package servlet;


import auxiliaryclasses.ConstantsClass;
import client.commandprocessor.PasswordEncoder;
import server.controllerforweb.XmlUtils;
import server.model.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(ConstantsClass.SERVLET_ADDRESS)
public class Servlet extends HttpServlet {
    private Connection connection;
    private DataSource dataSource;
    private PasswordEncoder encoder = PasswordEncoder.getInstance();
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    //private Controller controller = Controller.getInstance();

    private JournalContainer container;

    private List<Journal> journals;

    private Journal currentJournal;
    private Task currentTask;
    private User currentUser;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            this.dataSource = (DataSource) env.lookup("jdbc/cracker");
            this.connection = dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try { // todo после действий в sign in и sign up удалить этот блок, он тестовый
            String path = servletContext.getRealPath(ConstantsClass.JOURNALS_XML_FILE);
            container = xmlUtils.readJournalContainer(path);
            journals = container.getJournals();
        } catch (Exception e) {

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.DO_SIGN_IN:
                doSignIn(req, resp);
                break;
            case ConstantsClass.DO_SIGN_UP:
                doSignUp(req, resp);
                break;
            case ConstantsClass.DO_ADD_TASK:
                doAddTask(req, resp);
                break;
            case ConstantsClass.DO_EDIT_TASK:
                doEditTask(req, resp);
                break;
            case ConstantsClass.DO_CRUD_FROM_TASKS:
                doActionFromTasks(req, resp);
                break;
            case ConstantsClass.DO_CRUD_FROM_MAIN:
                doActionFromMain(req, resp);
                break;
            case ConstantsClass.DO_ADD_JOURNAL:
                doAddJournal(req, resp);
                break;
            case ConstantsClass.DO_EDIT_JOURNAL:
                doEditJournal(req, resp);
                break;
        }
    }

    private LinkedList<String> getJournalNames() {
        LinkedList<String> names = new LinkedList<>();
        for (Journal j : journals) {
            names.add(j.getName());
        }
        return names;
    }

    private void doActionFromTasks(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        switch (useraction) {
            case ConstantsClass.ADD:
                req.getRequestDispatcher(ConstantsClass.ADD_TASK_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.UPDATE: // имена в сессии
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    currentTask = currentJournal.getTasks().get(num);
                    req.getSession().setAttribute(ConstantsClass.CURRENT_STATUS, currentTask.getStatus());
                    try {
                        xmlUtils.writeTask(currentTask, req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
                    } catch (Exception e) {
                        resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                    }
                    boolean taskCorrect = xmlUtils.compareWithXsd(
                            req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE),
                            req.getServletContext().getRealPath(ConstantsClass.TASK_XSD_FILE));
                    if (taskCorrect) {
                        String t = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
                        req.setAttribute(ConstantsClass.CURRENT_TASK, t);
                        req.setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, currentJournal.getName());
                        req.getRequestDispatcher(ConstantsClass.EDIT_TASK_ADDRESS).forward(req, resp);
                    } else {
                        resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_TASK);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    Task task = currentJournal.getTasks().get(num);
                    int journalId = currentJournal.getId();
                    // todo вызов метода удаления по id (1/2 DONE)
                    // ! метод удаления должен вызываться по таске, так как она хранит ид журнала из которого ее стоит удалить
//                    try {
//                        controller.deleteTask(task);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                        //todo оповещение о неудаче
//                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_TASK);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.SORT:
                String sortColumn = req.getParameter(ConstantsClass.SORT_COLUMN); // name||description
                String sortCriteria = req.getParameter(ConstantsClass.SORT_CRITERIA); // asc||desc
                String filterLike = req.getParameter(ConstantsClass.FILTER_LIKE);
                String filterEquals = req.getParameter(ConstantsClass.FILTER_EQUALS);
                String sortResult;

                if (filterLike!= null && !filterLike.equals("")) {
                    if(!isLikeFilterCorrect(filterLike)) {
                        req.setAttribute(ConstantsClass.FILTER_LIKE, filterLike);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_LIKE);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionTasks(req, resp);
                    }
                } else if (filterEquals!= null && !filterEquals.equals("")) {
                    if(!isEqualsFilterCorrect(filterEquals)) {
                        req.setAttribute(ConstantsClass.FILTER_EQUALS, filterEquals);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_EQUALS);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionTasks(req, resp);
                    }
                }
                break;
        }
    }

    private void sortActionTasks (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        resp.getWriter().print("success");
        // todo формируешь новый journal container с отсортированными журанлами,
        // пишешь его в xml, сравниваешь ее с xsd (тут наверное какой то void метод).
        // Затем обновляешь container, journals(список журналов) и current journal этого класса(без этого все отвалится:) )
        // дальше то, что ниже

        //немного не понял логику, так что просто строка с хмл уже отсортированного контейнера
//                try {
//                    sortResult = controller.getSortedTasks(sortColumn, sortCriteria);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    //todo оповещение о неудаче
//                }

//        String xmlFile = xmlUtils.parseXmlToString(req.getServletContext().
//                getRealPath(ConstantsClass.JOURNAL_XML_FILE));
//        if (xmlFile != null)
//            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, xmlFile);
//        else
//            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
//
//        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
    }

    private void doAddTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);
                String planned = req.getParameter(ConstantsClass.PLANNED_DATE);
                String notification = req.getParameter(ConstantsClass.NOTIFICATION_DATE);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH) {
                    incorrectAddTask(req, resp, name, description, notification, planned, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH) {
                    incorrectAddTask(req, resp, name, description, notification, planned, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    Date plannedDate;
                    Date notificationDate;
                    try {
                        plannedDate = Date.valueOf(reverseDate(planned));
                        notificationDate = Date.valueOf(reverseDate(notification));

                        resp.getWriter().print("Successful");
                        // todo вызов метода добавления + формирование нового журнала, обновление container'a,
                        // journals и currentJournal, запись новых xml, их проверка, вставка новых xml в сессию,
                        // перенаправление на главную

                        // change и upload простовляютяс автоматически
                        // проверь верно ли я получаю ид журнала к которому относится задача
//                        try {
//                            controller.addTask(name, description, notificationDate, plannedDate, currentJournal.getId());
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                            //todo оповещение о неудаче
//                        }
                    } catch (IllegalArgumentException e) {
                        incorrectAddTask(req, resp, name, description, notification, planned, ConstantsClass.ERROR_DATE_PARSE);
                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_TASKS:
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void incorrectAddTask(HttpServletRequest req, HttpServletResponse resp, String name,
                                  String description, String notification, String planned, String message) throws ServletException, IOException {
        try {
            xmlUtils.writeTask(new Task(name, TaskStatus.Planned, description, notification, planned,
                            0, "", "", 0),
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
        } catch (Exception ex) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
        }
        boolean taskCorrect = xmlUtils.compareWithXsd(
                req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE),
                req.getServletContext().getRealPath(ConstantsClass.TASK_XSD_FILE));
        if (taskCorrect) {
            String t = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
            req.setAttribute(ConstantsClass.CURRENT_TASK, t);
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.ADD_TASK_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }

    private void doEditTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.SAVE:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);
                TaskStatus taskStatus = TaskStatus.valueOf(req.getParameter(ConstantsClass.STATUS)); // todo статус
                String planned = req.getParameter(ConstantsClass.PLANNED_DATE);
                String notification = req.getParameter(ConstantsClass.NOTIFICATION_DATE);
                int id = currentJournal.getId();
                int curTaskId = currentTask.getId();
                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH) {
                    incorrectEditTask(req, resp, name, description, planned, notification, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH) {
                    incorrectEditTask(req, resp, name, description, planned, notification, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {

                    Date plannedDate;
                    Date notificationDate;
                    try {
                        plannedDate = Date.valueOf(reverseDate(planned));
                        notificationDate = Date.valueOf(reverseDate(notification));
                        // todo не изменять upload и change и убрать их из jsp DONE

                        resp.getWriter().print("Successful");
                        // todo вызов метода изменения по id  + формирование нового журнала, обновление container'a,
                        //                        // journals и currentJournal, запись новых xml, их проверка, вставка новых xml в сессию,
                        //                        // перенаправление на главную

                        //логика метода в том, что он все делает по уже поменяной таски в модели, так что подставь нужный ид таски и журнала
                        int journalId = 0; // todo изменять поля таски по айди из журнала, а не новую
                        int taskId = currentTask.getId();
//                        String editedTaskString = controller.getTask(journalId, taskId);
//                        //делаем из нее объект задачи, обновляем сетерами поля и отправляем
//                        Task editedTask = null;
//                        try {
//                            controller.editTask(editedTask);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
                        //req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
//                            //todo оповещение; предлагаю отправлять оповещение об exception, как в строчке выше

//                        }
                    } catch (IllegalArgumentException e) {
                        incorrectEditTask(req, resp, name, description, planned, notification, ConstantsClass.ERROR_DATE_PARSE);
                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_TASKS:
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void incorrectEditTask(HttpServletRequest req, HttpServletResponse resp, String name,
                                   String description, String planned, String notification, String message)
            throws ServletException, IOException {
        try {
            xmlUtils.writeTask(new Task(name, currentTask.getStatus(),
                            description, notification, planned,
                            0, currentTask.getUpload(), currentTask.getChange(), currentJournal.getId()),
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
        } catch (Exception ex) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
        }
        boolean taskCorrect = xmlUtils.compareWithXsd(
                req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE),
                req.getServletContext().getRealPath(ConstantsClass.TASK_XSD_FILE));
        if (taskCorrect) {
            String t = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
            req.setAttribute(ConstantsClass.CURRENT_TASK, t);
            req.setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, currentJournal.getName());
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.EDIT_TASK_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }

    // 12-08-1980
    private String reverseDate(String date) {
        if (date.length() == 0) return date;
        StringBuilder builder = new StringBuilder();
        int firstDash = date.lastIndexOf('-');
        if (firstDash == -1) return date;
        builder.append(date.substring(firstDash + 1, date.length()));
        builder.append('-');
        int secondDash = date.indexOf('-');
        if (secondDash == firstDash) return date;
        builder.append(date.substring(secondDash + 1, firstDash));
        builder.append('-');
        builder.append(date.substring(0, secondDash));
        return builder.toString();
    }

    private void doSignIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String login;
        String password;
        String encryptedPassword;
        switch (useraction) {
            case ConstantsClass.DO_SIGN_IN:
                login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
                password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
//                        try {
//                            encryptedPassword = encoder.encode(password);
//                        } catch (NoSuchAlgorithmException e) {
//                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
//                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
//                            break;
//                        }
//
//                        if (authorizer.isUserDataCorrect(login, encryptedPassword)) {
//                            String xmlFile = xmlUtils.parseXmlToString(req.getServletContext().
//                            getRealPath(ConstantsClass.JOURNALS_XML_FILE));
//                    if (xmlFile != null)
//                        req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, xmlFile);
//                    else
//                        resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
//
//                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
//                        } else {
//                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
//                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
//                        } // todo раскомменти как соединишь authorizer'a с дао, а то, что ниже в блоке удали

                if (login.equals("1") && password.equals("1")) {
                    String xmlFile = xmlUtils.parseXmlToString(req.getServletContext().
                            getRealPath(ConstantsClass.JOURNALS_XML_FILE));
                    if (xmlFile != null)
                        req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, xmlFile);
                    else
                        resp.getWriter().print(ConstantsClass.ERROR_XML_READING);

                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
                    req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DO_SIGN_UP:
                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void doSignUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
//        String password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
//        String encryptedPassword;
//
//        if (authorizer.isSuchLoginExists(login)) {
//            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.EXIST_LOGIN);
//            req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
//        } else {
//            try {
//                encryptedPassword = encoder.encode(password);
//            } catch (NoSuchAlgorithmException e) {
//                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
//                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
//            }
//            authorizer.addUser(login, password);
//
        // todo после добавления нового пользователя нужно будет сформировать journal container, обновить местный container и journals + сформировать и проверить xml, сунуть их в сессию. в sign in то же самое

//            String xmlFile = xmlUtils.parseXmlToString(req.getServletContext().
//                    getRealPath(ConstantsClass.JOURNALS_XML_FILE));
//            if (xmlFile != null)
//                req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, xmlFile);
//            else
//                resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
//
//            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
////        }
    }

    private void doAddJournal(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH) {
                    incorrectAddJournal(req, resp, name, description, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH) {
                    incorrectAddJournal(req, resp, name, description, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    // todo добавляешь новый журнал по name, description
                    int userId = 0;// todo юзера верни после signin/signup и запиши его в локальную пер currentUser. Потом его айди бери просто
//                    try {
//                        controller.addJournal(name, description, userId);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void incorrectAddJournal(HttpServletRequest req, HttpServletResponse resp,
                                     String name, String description, String message)
            throws ServletException, IOException {
        try {
            xmlUtils.writeJournal(new Journal(name, description),
                    req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
        } catch (Exception e) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
        }
        boolean journalCorrect = xmlUtils.compareWithXsd(
                req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE),
                req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XSD_FILE));
        if (journalCorrect) {
            String j = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
            req.setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.ADD_JOURNAL_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }

    private void doEditJournal(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.SAVE:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH) {
                    incorrectEditJournal(req, resp, name, description, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH) {
                    incorrectEditJournal(req, resp, name, description, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    int id = (int) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_ID);
                    // todo вызов метода изменения журнала по id
                    //наверное можно просто каррент джорнал отправлять, меняя у него все поля //todo да, можно
//                    try {
//                        controller.editJournal(null);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void incorrectEditJournal(HttpServletRequest req, HttpServletResponse resp,
                                      String name, String description, String message)
            throws ServletException, IOException {
        try {
            xmlUtils.writeJournal(new Journal(name, description),
                    req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
        } catch (Exception e) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
        }
        boolean journalCorrect = xmlUtils.compareWithXsd(
                req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE),
                req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XSD_FILE));
        if (journalCorrect) {
            String j = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
            req.setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }

    private void doActionFromMain(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        switch (useraction) {
            case ConstantsClass.ADD:
                req.getRequestDispatcher(ConstantsClass.ADD_JOURNAL_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.UPDATE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    currentJournal = journals.get(num);
                    try {
                        xmlUtils.writeJournal(currentJournal,
                                req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
                    } catch (Exception e) {
                        resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                    }
                    boolean journalCorrect = xmlUtils.compareWithXsd(
                            req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE),
                            req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XSD_FILE));
                    if (journalCorrect) {
                        String j = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
                        req.setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
                        req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL_ID, journals.get(num).getId());
                        req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE: // норм
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    int id = journals.get(num).getId();
                    // todo вызов метода удаления журнала по id
//                    try {
//                        controller.deleteJournal(id);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.CHOOSE: // записали в сессию текущий журнал и имена
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null) {
                    currentJournal = journals.get(Integer.parseInt(usernumber));
                    try {
                        xmlUtils.writeJournal(currentJournal, req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
                        xmlUtils.writeNames(new JournalNamesContainer(getJournalNames()),
                                req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE));
                    } catch (Exception e) {
                        resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                    }
                    boolean journalCorrect = xmlUtils.compareWithXsd(
                            req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE),
                            req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XSD_FILE));
                    boolean namesCorrect = xmlUtils.compareWithXsd(
                            req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE),
                            req.getServletContext().getRealPath(ConstantsClass.NAMES_XSD_FILE));
                    if (journalCorrect && namesCorrect) {
                        String j = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
                        String n = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE));
                        req.getSession().setAttribute(ConstantsClass.JOURNAL_NAMES, n);
                        req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.SORT:
                String sortColumn = req.getParameter(ConstantsClass.SORT_COLUMN); // name||description
                String sortCriteria = req.getParameter(ConstantsClass.SORT_CRITERIA); // asc||desc
                String filterLike = req.getParameter(ConstantsClass.FILTER_LIKE);
                String filterEquals = req.getParameter(ConstantsClass.FILTER_EQUALS);

                if (filterLike != null && !filterLike.equals("")) {
                    if(!isLikeFilterCorrect(filterLike)) {
                        req.setAttribute(ConstantsClass.FILTER_LIKE, filterLike);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_LIKE);
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionJournals(req, resp);
                    }
                } else if (filterEquals!= null && !filterEquals.equals("")) {
                    if(!isEqualsFilterCorrect(filterEquals)) {
                        req.setAttribute(ConstantsClass.FILTER_EQUALS, filterEquals);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_EQUALS);
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionJournals(req, resp);
                    }
                }
                break;
        }
    }

    private void sortActionJournals(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().print("success");
        String sortedJournals;
        // todo формируешь новый journal container с отсортированными журанлами,
        // пишешь его в xml, сравниваешь ее с xsd (тут наверное какой то void метод).
        // Затем обновляешь container и journals(список журналов) этого класса(без этого все отвалится:) )
        // дальше то, что ниже

        //аналог с тасками
//                try {
//                    sortedJournals = controller.getSortedJournals(sortColumn, sortCriteria);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }

//        String xmlFile = xmlUtils.parseXmlToString(req.getServletContext().
//                getRealPath(ConstantsClass.JOURNALS_XML_FILE));
//        if (xmlFile != null)
//            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, xmlFile);
//        else
//            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
//
//        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
    }

    private boolean isLikeFilterCorrect(String like) {
        Pattern pattern = Pattern.compile("[%_]*[A-Za-z0-9]+[%_]*");
        Matcher matcher = pattern.matcher(like);

        return matcher.matches();
    }

    private boolean isEqualsFilterCorrect(String equals) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        Matcher matcher = pattern.matcher(equals);

        return matcher.matches();
    }
}
