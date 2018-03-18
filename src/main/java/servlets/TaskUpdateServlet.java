package servlets;

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

@WebServlet(ConstantsClass.TASK_UPDATE_SERVLET_ADDRESS)
public class TaskUpdateServlet extends HttpServlet {
    private Controller controller;
    private DataUpdateUtil updateUtil;
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    private PatternChecker patternChecker = PatternChecker.getInstance();

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
        }
    }

    private void doAddTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            controller = Controller.getInstance();
            updateUtil = DataUpdateUtil.getInstance();
        } catch (ControllerActionException e) {
            //todo обсудить
            e.printStackTrace();
        }
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
                                journalId = controller.getJournal(journalName).getId();
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
        boolean taskCorrect = false;
        try {
            taskCorrect = xmlUtils.compareWithXsd(
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE),
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XSD_FILE));
        } catch (Exception e) {
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
            req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
        }
        if (taskCorrect) {
            String t = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
            req.setAttribute(ConstantsClass.CURRENT_TASK, t);
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }

    private void doEditTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            controller = Controller.getInstance();
            updateUtil = DataUpdateUtil.getInstance();
        } catch (ControllerActionException e) {
            //todo обсудить
            e.printStackTrace();
        }
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
                            Task currentTask = (Task) req.getSession().getAttribute(ConstantsClass.CURRENT_TASK);
                            controller.editTask(currentTask.getId(), currentTask.getJournalId(), name, taskStatus,
                                    description, notificationDate, plannedDate, journalName);
                            updateUtil.updateTasks(req, resp, currentJournal);
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
            Task currentTask = (Task) req.getSession().getAttribute(ConstantsClass.CURRENT_TASK);
            xmlUtils.writeTask(new Task(name, currentTask.getStatus(),
                            description, notification, planned,
                            0, currentTask.getUpload(), currentTask.getChange(), currentJournal.getId()),
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
        } catch (Exception ex) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
        }
        boolean taskCorrect = false;
        try {
            taskCorrect = xmlUtils.compareWithXsd(
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE),
                    req.getServletContext().getRealPath(ConstantsClass.TASK_XSD_FILE));
        } catch (Exception e) {
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
            req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
        }
        if (taskCorrect) {
            String t = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.TASK_XML_FILE));
            req.setAttribute(ConstantsClass.CURRENT_TASK, t);
            req.setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, currentJournal.getName());
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
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
