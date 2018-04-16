package servlets;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.DownloadConstants;
import server.beans.EIBeanLocal;
import server.controller.Controller;
import server.controller.XmlUtils;
import server.exceptions.ControllerActionException;
import server.model.Journal;
import server.model.Task;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@WebServlet(ConstantsClass.TASK_SERVLET_ADDRESS)
@MultipartConfig
public class TaskServlet extends HttpServlet {
    private Controller controller;
    private DataUpdateUtil updateUtil;
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    private PatternChecker patternChecker = PatternChecker.getInstance();
    private ImportExportManager importExportManager = ImportExportManager.getInstance();

    private Task currentTask;

    @EJB
    EIBeanLocal ExportImportBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.DO_CRUD_FROM_TASKS:
                doActionFromTasks(req, resp);
                break;
        }
    }

    private void doActionFromTasks(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            controller = Controller.getInstance();
            updateUtil = DataUpdateUtil.getInstance();
        } catch (ControllerActionException e) {
            resp.getWriter().print(ConstantsClass.ERROR_LAZY_MESSAGE);
        }
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
        switch (useraction) {
            case ConstantsClass.ADD:
                req.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.TRUE);
                req.getSession().setAttribute(ConstantsClass.IS_EDIT, Boolean.FALSE);
                req.getSession().setAttribute(ConstantsClass.IS_RENAME, Boolean.FALSE);
                req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, currentJournal.getName());
                req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.UPDATE: // имена в сессии
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    currentTask = controller.getTask(currentJournal.getId(), num);
                    req.getSession().setAttribute(ConstantsClass.IS_EDIT, Boolean.TRUE);
                    req.getSession().setAttribute(ConstantsClass.CURRENT_TASK, currentTask);
                    req.getSession().setAttribute(ConstantsClass.CURRENT_STATUS, currentTask.getStatus());
                    String t = null;
                    try {
                        t = xmlUtils.marshalToXmlString(Task.class, currentTask);
                    } catch (Exception e) {
                        resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                    }
                    req.setAttribute(ConstantsClass.CURRENT_TASK_XML, t);
                    req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, currentJournal.getName());
                    req.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.FALSE);
                    req.getSession().setAttribute(ConstantsClass.IS_RENAME, Boolean.FALSE);
                    req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_TASK);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    currentTask = controller.getTask(currentJournal.getId(), num);
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
            case ConstantsClass.RENAME:
                req.getSession().setAttribute(ConstantsClass.IS_RENAME, Boolean.TRUE);
                req.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.FALSE);
                req.getSession().setAttribute(ConstantsClass.IS_EDIT, Boolean.FALSE);
                req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, currentJournal.getName());
                List<Integer> renameIds = importExportManager.createIDList(req.getParameterValues(ConstantsClass.USERNUMBER));
                req.getSession().setAttribute(ConstantsClass.RENAMENUMBER, renameIds);
                req.getRequestDispatcher(ConstantsClass.UPDATE_TASKS_ADDRESS).forward(req, resp);
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
            case ConstantsClass.SHOW_ALL:
                updateUtil.updateTasks(req, resp, currentJournal);
                break;
            case ConstantsClass.RELOAD:
                updateUtil.updateJournals(req, resp);
                break;
            case ConstantsClass.IMPORT:
                Part part = req.getPart(ConstantsClass.IMPORT_PARAMETER);
                importExportManager.doImport(part, req, resp);
                break;
            case ConstantsClass.EXPORT:
                String[] checkBoxes = req.getParameterValues(ConstantsClass.USERNUMBER);
                try {
                    String exportXml = ExportImportBean.exportData(null, importExportManager.createIDList(checkBoxes));
                    importExportManager.downloadAction(req, resp, exportXml);
                } catch (Exception e) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_LAZY_MESSAGE);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                }
                break;
        }
    }

    private void sortActionTasks(HttpServletRequest req, HttpServletResponse resp, String sortColumn,
                                 String sortCriteria, String filterLike, String filterEquals)
            throws ServletException, IOException {
        Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
        String sortedTasks = null;
        if (filterEquals == null && filterLike == null) {
            try {
                Journal tasks = controller.getSortedTasks(currentJournal.getId(), sortColumn, sortCriteria);
                try {
                    sortedTasks = xmlUtils.marshalToXmlString(tasks.getClass(), tasks);
                } catch (JAXBException e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
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
                Journal tasks = controller.getFilteredTasksByEquals(currentJournal.getId(), sortColumn, filterEquals, sortCriteria);
                try {
                    sortedTasks = xmlUtils.marshalToXmlString(tasks.getClass(), tasks);
                } catch (JAXBException e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
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
                Journal tasks = controller.getFilteredTasksByPattern(currentJournal.getId(), sortColumn, filterLike, sortCriteria);
                try {
                    sortedTasks = xmlUtils.marshalToXmlString(tasks.getClass(), tasks);
                } catch (JAXBException e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
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
}
