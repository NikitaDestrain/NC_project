package servlet;

import auxiliaryclasses.ConstantsClass;
import server.controller.PasswordEncoder;
import database.postgresql.PostgreSQLDAOFactory;
import server.controller.Controller;
import server.controller.UserAuthorizer;
import server.controller.XmlUtils;
import server.exceptions.ControllerActionException;
import server.model.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(ConstantsClass.SERVLET_ADDRESS)
public class Servlet extends HttpServlet {
    private PasswordEncoder encoder = PasswordEncoder.getInstance();
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    private Controller controller;
    private PostgreSQLDAOFactory dbFactory;
    private JournalContainer container;
    private List<Journal> journals;
    private Journal currentJournal;
    private Task currentTask;
    private User currentUser;
    private UserAuthorizer authorizer = UserAuthorizer.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        dbFactory = PostgreSQLDAOFactory.getInstance(config.getServletContext().getRealPath(ConstantsClass.SCRIPT_FILE));
        controller = Controller.getInstance();
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

    private void updateJournals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        journals = controller.getJournalContainer().getJournals();
        String updatedJournals = controller.getJournals();
        if (updatedJournals != null) {
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, updatedJournals);
            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
    }

    private void updateSortedJournals(HttpServletRequest req, HttpServletResponse resp, String sortedJournals) throws ServletException, IOException {
        //todo то же самое что с апдейт таскс
        if (sortedJournals != null) {
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, sortedJournals);
            req.setAttribute(ConstantsClass.IS_SORTED, true);
            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
    }

    private void updateTasks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String updatedJournal = controller.getTasks(currentJournal.getId());
        if (updatedJournal != null) {
            currentJournal = controller.getJournalObject(currentJournal.getId());
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, updatedJournal);
            req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
    }

    private void updateSortedTasks(HttpServletRequest req, HttpServletResponse resp, String sortedTasks) throws ServletException, IOException {
        if (sortedTasks != null) {
            //todo здесь нужно взять данные из хмл, так как модель не меняется, чтобы не затереть при паттерне
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, sortedTasks);
            req.setAttribute(ConstantsClass.IS_SORTED, true);
            req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
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
                    currentTask = controller.getTaskObject(currentJournal.getId(), num);
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
                    currentTask = controller.getTaskObject(currentJournal.getId(), num);
                    try {
                        controller.deleteTask(currentTask);
                        updateTasks(req, resp);
                    } catch (ControllerActionException e) {
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    }
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

                if (filterLike != null && !filterLike.equals("")) {
                    if (!isLikeFilterCorrect(filterLike)) {
                        req.setAttribute(ConstantsClass.FILTER_LIKE, filterLike);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_LIKE);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionTasks(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else if (filterEquals != null && !filterEquals.equals("")) {
                    if (!isEqualsFilterCorrect(filterEquals)) {
                        req.setAttribute(ConstantsClass.FILTER_EQUALS, filterEquals);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_EQUALS);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionTasks(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else {
                    sortActionTasks(req, resp, sortColumn, sortCriteria, null, null);
                }
                break;
            case ConstantsClass.SHOW_ALL :
                updateTasks(req, resp);
                break;
        }
    }

    private void sortActionTasks(HttpServletRequest req, HttpServletResponse resp, String sortColumn,
                                 String sortCriteria, String filterLike, String filterEquals)
            throws ServletException, IOException {
        String sortedTasks;
        if (filterEquals == null && filterLike == null) {
            try {
                sortedTasks = controller.getSortedTasks(currentJournal.getId(), sortColumn, sortCriteria);
                if (sortedTasks == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateSortedTasks(req, resp, sortedTasks);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
            }
        } else if (filterEquals != null) {
            try {
                sortedTasks = controller.getFilteredTasksByEquals(currentJournal.getId(), sortColumn, filterEquals, sortCriteria);
                if (sortedTasks == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateSortedTasks(req, resp, sortedTasks);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
            }
        } else if (filterLike != null) {
            try {
                sortedTasks = controller.getFilteredTasksByPattern(currentJournal.getId(), sortColumn, filterLike, sortCriteria);
                if (sortedTasks == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateSortedTasks(req, resp, sortedTasks);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
            }
        }
        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
    }

    private void doAddTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);
                String planned = req.getParameter(ConstantsClass.PLANNED_DATE);
                String notification = req.getParameter(ConstantsClass.NOTIFICATION_DATE);
                String journalName = req.getParameter(ConstantsClass.JOURNAL_NAME);
                int journalId = -1;

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !isLatinChars(name)) {
                    incorrectAddTask(req, resp, name, description, notification, planned, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !isCorrectDescription(description)) {
                    incorrectAddTask(req, resp, name, description, notification, planned, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    Date plannedDate;
                    Date notificationDate;
                    try {
                        plannedDate = Date.valueOf(reverseDate(planned));
                        notificationDate = Date.valueOf(reverseDate(notification));

                        try {
                            if (!journalName.equals("")) {
                                journalId = controller.getJournalObject(journalName).getId();
                            }
                            controller.addTask(name, description, notificationDate, plannedDate,
                                    journalId == -1 ? currentJournal.getId() : journalId);
                            updateTasks(req, resp);
                        } catch (ControllerActionException e) {
                            incorrectAddTask(req, resp, name, description, notification, planned, e.getMessage());
                        }
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
                String status = req.getParameter(ConstantsClass.STATUS);
                TaskStatus taskStatus;
                String planned = req.getParameter(ConstantsClass.PLANNED_DATE);
                String notification = req.getParameter(ConstantsClass.NOTIFICATION_DATE);
                String journalName = req.getParameter(ConstantsClass.JOURNAL_NAME);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !isLatinChars(name)) {
                    incorrectEditTask(req, resp, name, description, planned, notification, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !isCorrectDescription(description)) {
                    incorrectEditTask(req, resp, name, description, planned, notification, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    Date plannedDate;
                    Date notificationDate;
                    try {
                        plannedDate = Date.valueOf(reverseDate(planned));
                        notificationDate = Date.valueOf(reverseDate(notification));

                        if (!status.equals("")) {
                            taskStatus = TaskStatus.valueOf(status);
                        } else
                            taskStatus = null;

                        try {
                            controller.editTask(currentTask.getId(), currentTask.getJournalId(), name, taskStatus,
                                    description, notificationDate, plannedDate, journalName);
                            updateTasks(req, resp);
                        } catch (ControllerActionException e) {
                            incorrectEditTask(req, resp, name, description, planned, notification, e.getMessage());
                        }
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
                if (!isCorrectLogin(login) || !isCorrectLogin(password) || login.length() > ConstantsClass.LOGIN_FIELD_LENGTH) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_AUTH);
                    req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
                    req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
                } else {
                    try {
                        encryptedPassword = encoder.encode(password);
                    } catch (NoSuchAlgorithmException e) {
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
                        req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
                        req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
                        break;
                    }
                    currentUser = controller.signInUser(login, encryptedPassword);
                    if (currentUser != null) {
                        updateJournals(req, resp);
                    } else {
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
                        req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
                        req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
                    }
                    break;
                }
            case ConstantsClass.DO_SIGN_UP:
                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void doSignUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
        String password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
        String encryptedPassword = null;
        if (!isCorrectLogin(login) || !isCorrectLogin(password) || login.length() > ConstantsClass.LOGIN_FIELD_LENGTH) {
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_AUTH);
            req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
            req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
        } else {
            try {
                encryptedPassword = encoder.encode(password);
            } catch (NoSuchAlgorithmException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
                req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
            }
            try {
                controller.addUser(login, encryptedPassword, ConstantsClass.USER_ROLE);
                currentUser = controller.signInUser(login, encryptedPassword);
                if (currentUser != null)
                    updateJournals(req, resp);
                else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_UP);
                    req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
                    req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
            }
        }
    }

    private void doAddJournal(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !isLatinChars(name)) {
                    incorrectAddJournal(req, resp, name, description, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !isCorrectDescription(description)) {
                    incorrectAddJournal(req, resp, name, description, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    int userId = currentUser.getId();
                    try {
                        controller.addJournal(name, description, userId);
                        updateJournals(req, resp);
                    } catch (ControllerActionException e) {
                        incorrectAddJournal(req, resp, name, description, e.getMessage());
                    }
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

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !isLatinChars(name)) {
                    incorrectEditJournal(req, resp, name, description, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !isCorrectDescription(description)) {
                    incorrectEditJournal(req, resp, name, description, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    try {
                        controller.editJournal(currentJournal.getId(), name, description);
                        updateJournals(req, resp);
                    } catch (ControllerActionException e) {
                        incorrectEditJournal(req, resp, name, description, e.getMessage());
                    }
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
                    currentJournal = controller.getJournalObject(num);
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
                        req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL_ID, currentJournal);
                        req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    try {
                        controller.deleteJournal(num);
                        updateJournals(req, resp);
                    } catch (ControllerActionException e) {
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.CHOOSE: // записали в сессию текущий журнал и имена
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null) {
                    currentJournal = controller.getJournalObject(Integer.parseInt(usernumber));
                    try {
                        xmlUtils.writeJournal(currentJournal, req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
                        xmlUtils.writeNames(controller.getJournalNamesContainer(),
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
                    if (!isLikeFilterCorrect(filterLike)) {
                        req.setAttribute(ConstantsClass.FILTER_LIKE, filterLike);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_LIKE);
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionJournals(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else if (filterEquals != null && !filterEquals.equals("")) {
                    if (!isEqualsFilterCorrect(filterEquals)) {
                        req.setAttribute(ConstantsClass.FILTER_EQUALS, filterEquals);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_EQUALS);
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionJournals(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else {
                    sortActionJournals(req, resp, sortColumn, sortCriteria, null, null);
                }
                break;
            case ConstantsClass.SHOW_ALL :
                updateJournals(req, resp);
                break;
        }
    }

    private void sortActionJournals(HttpServletRequest req, HttpServletResponse resp, String sortColumn,
                                    String sortCriteria, String filterLike, String filterEquals)
            throws ServletException, IOException {
        String sortedJournals;
        if (filterEquals == null && filterLike == null) {
            try {
                sortedJournals = controller.getSortedJournals(sortColumn, sortCriteria);
                if (sortedJournals == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateSortedJournals(req, resp, sortedJournals);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
            }
        } else if (filterEquals != null) {
            try {
                sortedJournals = controller.getFilteredJournalsByEquals(sortColumn, filterEquals, sortCriteria);
                if (sortedJournals == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateSortedJournals(req, resp, sortedJournals);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
            }
        } else if (filterLike != null) {
            try {
                sortedJournals = controller.getFilteredJournalsByPattern(sortColumn, filterLike, sortCriteria);
                if (sortedJournals == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateSortedJournals(req, resp, sortedJournals);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
            }
        }
        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
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

    private boolean isLatinChars(String s) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+\\s*[A-Za-z0-9]*"); // a342aa
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    private boolean isCorrectLogin(String s) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+"); // a342aa
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    private boolean isCorrectDescription(String s) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]*\\s*[A-Za-z0-9]*"); // a342aa
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
