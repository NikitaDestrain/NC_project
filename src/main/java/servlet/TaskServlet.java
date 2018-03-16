package servlet;

import auxiliaryclasses.ConstantsClass;
import server.controller.Controller;
import server.controller.XmlUtils;
import server.exceptions.ControllerActionException;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet(ConstantsClass.TASK_SERVLET_ADDRESS)
public class TaskServlet extends HttpServlet {
    private Controller controller = Controller.getInstance();
    private DataUpdateUtil updateUtil = DataUpdateUtil.getInstance();
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    private PatternChecker patternChecker = PatternChecker.getInstance();

    private Task currentTask;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.DO_ADD_TASK:
                doAddTask(req, resp);
                break;
            case ConstantsClass.DO_EDIT_TASK:
                doEditTask(req, resp);
                break;
            case ConstantsClass.DO_CRUD_FROM_TASKS:
                doActionFromTasks(req, resp);
                break;
        }
    }

    private void doActionFromTasks(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
        switch (useraction) {
            case ConstantsClass.ADD:
                req.getRequestDispatcher(ConstantsClass.ADD_TASK_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
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
                        updateUtil.updateTasks(req, resp, currentJournal);
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
                    if (!patternChecker.isLikeFilterCorrect(filterLike)) {
                        req.setAttribute(ConstantsClass.FILTER_LIKE, filterLike);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_LIKE);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionTasks(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else if (filterEquals != null && !filterEquals.equals("")) {
                    if (!patternChecker.isEqualsFilterCorrect(filterEquals)) {
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
                updateUtil.updateTasks(req, resp, currentJournal);
                break;
            case ConstantsClass.RELOAD :
                updateUtil.updateJournals(req, resp);
                break;
        }
    }

    private void sortActionTasks(HttpServletRequest req, HttpServletResponse resp, String sortColumn,
                                 String sortCriteria, String filterLike, String filterEquals)
            throws ServletException, IOException {
        Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
        String sortedTasks;
        if (filterEquals == null && filterLike == null) {
            try {
                sortedTasks = controller.getSortedTasks(currentJournal.getId(), sortColumn, sortCriteria);
                if (sortedTasks == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateUtil.updateSortedTasks(req, resp, sortedTasks);
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
                    updateUtil.updateSortedTasks(req, resp, sortedTasks);
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
                    updateUtil.updateSortedTasks(req, resp, sortedTasks);
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
        Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);
                String planned = req.getParameter(ConstantsClass.PLANNED_DATE);
                String notification = req.getParameter(ConstantsClass.NOTIFICATION_DATE);
                String journalName = req.getParameter(ConstantsClass.JOURNAL_NAME);
                int journalId = -1;

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !patternChecker.isLatinChars(name)) {
                    incorrectAddTask(req, resp, name, description, notification, planned, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !patternChecker.isCorrectDescription(description)) {
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
                            updateUtil.updateTasks(req, resp, currentJournal);
                        } catch (ControllerActionException e) {
                            incorrectAddTask(req, resp, name, description, notification, planned, e.getMessage());
                        }
                    } catch (IllegalArgumentException e) {
                        incorrectAddTask(req, resp, name, description, notification,
                                planned, ConstantsClass.ERROR_DATE_PARSE);
                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
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
        Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
        switch (useraction) {
            case ConstantsClass.SAVE:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);
                String status = req.getParameter(ConstantsClass.STATUS);
                TaskStatus taskStatus;
                String planned = req.getParameter(ConstantsClass.PLANNED_DATE);
                String notification = req.getParameter(ConstantsClass.NOTIFICATION_DATE);
                String journalName = req.getParameter(ConstantsClass.JOURNAL_NAME);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !patternChecker.isLatinChars(name)) {
                    incorrectEditTask(req, resp, name, description, planned, notification,
                            ConstantsClass.ERROR_NAME_LENGTH, currentJournal);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !patternChecker.isCorrectDescription(description)) {
                    incorrectEditTask(req, resp, name, description, planned, notification,
                            ConstantsClass.ERROR_DESCRIPTION_LENGTH, currentJournal);
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
                            updateUtil.updateTasks(req, resp,currentJournal);
                        } catch (ControllerActionException e) {
                            incorrectEditTask(req, resp, name, description, planned, notification, e.getMessage(), currentJournal);
                        }
                    } catch (IllegalArgumentException e) {
                        incorrectEditTask(req, resp, name, description, planned, notification, ConstantsClass.ERROR_DATE_PARSE, currentJournal);
                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_TASKS:
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void incorrectEditTask(HttpServletRequest req, HttpServletResponse resp, String name,
                                   String description, String planned, String notification, String message, Journal currentJournal)
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
}
