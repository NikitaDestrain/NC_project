package servlets;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.DownloadConstants;
import server.beans.EIBeanLocal;
import server.controller.Controller;
import server.controller.XmlUtils;
import server.exceptions.ControllerActionException;
import server.model.Journal;
import server.model.JournalContainer;

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

@WebServlet(ConstantsClass.JOURNAL_SERVLET_ADDRESS)
@MultipartConfig
public class JournalServlet extends HttpServlet {
    private Controller controller;
    private DataUpdateUtil updateUtil;
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    private PatternChecker patternChecker = PatternChecker.getInstance();
    private ImportExportManager importExportManager = ImportExportManager.getInstance();

    private Journal currentJournal;

    @EJB
    EIBeanLocal ExportImportBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.DO_CRUD_FROM_JOURNAL:
                doActionFromMain(req, resp);
                break;
        }
    }

    private void doActionFromMain(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            updateUtil = DataUpdateUtil.getInstance();
            controller = Controller.getInstance();
        } catch (ControllerActionException e) {
            resp.getWriter().print(ConstantsClass.ERROR_LAZY_MESSAGE);
        }
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        switch (useraction) {
            case ConstantsClass.ADD:
                req.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.TRUE);
                req.getRequestDispatcher(ConstantsClass.UPDATE_JOURNALS_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.UPDATE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    String journal = null;
                    try {
                        Journal journalObject = controller.getJournal(num);
                        journal = xmlUtils.marshalToXmlString(journalObject.getClass(), journalObject);
                    } catch (JAXBException e) {
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_LAZY_MESSAGE);
                        req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                    }
                    req.setAttribute(ConstantsClass.JOURNAL_PARAMETER, journal);
                    req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL, controller.getJournal(num));
                    req.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.FALSE);
                    req.getRequestDispatcher(ConstantsClass.UPDATE_JOURNALS_ADDRESS).forward(req, resp);
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    try {
                        controller.deleteJournal(num);
                        updateUtil.updateJournals(req, resp);
                    } catch (ControllerActionException e) {
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                        req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                    }
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.CHOOSE: // записали в сессию текущий журнал и имена
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null) {
                    currentJournal = controller.getJournal(Integer.parseInt(usernumber));
                    req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL, currentJournal);
                    try {
                        xmlUtils.writeNames(controller.getJournalNamesContainer(),
                                req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE));
                    } catch (Exception e) {
                        resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                    }
                    String n = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE));
                    String xslNames = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.NAMES_XSL));
                    String j = null;
                    try {
                        j = xmlUtils.marshalToXmlString(Journal.class, currentJournal);
                    } catch (JAXBException e) {
                        req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, "");
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_XML_WRITING);
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    }
                    req.getSession().setAttribute(ConstantsClass.JOURNAL_NAMES, n);
                    req.getSession().setAttribute(ConstantsClass.XSL_JOURNAL_NAMES_ATTRIBUTE, xslNames);
                    req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
                    req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
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
                        req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionJournals(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else if (filterEquals != null && !filterEquals.equals("")) {
                    if (!patternChecker.isEqualsFilterCorrect(filterEquals)) {
                        req.setAttribute(ConstantsClass.FILTER_EQUALS, filterEquals);
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_FILTER_EQUALS);
                        req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                    } else {
                        sortActionJournals(req, resp, sortColumn, sortCriteria, filterLike, filterEquals);
                    }
                } else {
                    sortActionJournals(req, resp, sortColumn, sortCriteria, null, null);
                }
                break;
            case ConstantsClass.SHOW_ALL:
                updateUtil.updateJournals(req, resp);
                break;
            case ConstantsClass.RELOAD:
                updateUtil.updateJournals(req, resp);
                break;
            case ConstantsClass.IMPORT:
                //req.getRequestDispatcher(ConstantsClass.JOURNALS_XSL_ADDRESS).forward(req, resp);
                Part part = req.getPart(ConstantsClass.IMPORT_PARAMETER);
                importExportManager.doImport(part, req, resp);
                break;
            case ConstantsClass.EXPORT:
                String[] checkBoxes = req.getParameterValues(ConstantsClass.USERNUMBER);
                try {
                    List<Integer> listIds = importExportManager.createIDList(checkBoxes);
                    String exportXml = ExportImportBean.exportData(listIds, null);
                    downloadAction(req, resp, exportXml);
                } catch (Exception e) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_LAZY_MESSAGE);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                }
                break;
        }
    }

    private void sortActionJournals(HttpServletRequest req, HttpServletResponse resp, String sortColumn,
                                    String sortCriteria, String filterLike, String filterEquals)
            throws ServletException, IOException {
        String sortedJournals = null;
        if (filterEquals == null && filterLike == null) {
            try {
                JournalContainer sortedJournalsContainer = controller.getSortedJournals(sortColumn, sortCriteria);
                try {
                    sortedJournals = xmlUtils.marshalToXmlString(sortedJournalsContainer.getClass(), sortedJournalsContainer);
                } catch (JAXBException e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
                if (sortedJournals == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateUtil.updateSortedJournals(req, resp, sortedJournals);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
            }
        } else if (filterEquals != null) {
            try {
                JournalContainer sortedJournalsContainer = controller.getFilteredJournalsByEquals(sortColumn, filterEquals, sortCriteria);
                try {
                    sortedJournals = xmlUtils.marshalToXmlString(sortedJournalsContainer.getClass(), sortedJournalsContainer);
                } catch (JAXBException e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
                if (sortedJournals == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateUtil.updateSortedJournals(req, resp, sortedJournals);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
            }
        } else if (filterLike != null) {
            try {
                JournalContainer sortedJournalsContainer = controller.getFilteredJournalsByPattern(sortColumn, filterLike, sortCriteria);
                try {
                    sortedJournals = xmlUtils.marshalToXmlString(sortedJournalsContainer.getClass(), sortedJournalsContainer);
                } catch (JAXBException e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
                if (sortedJournals == null) {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_NO_DATA_FOR_THIS_CRITERION);
                    req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
                } else {
                    updateUtil.updateSortedJournals(req, resp, sortedJournals);
                }
            } catch (ControllerActionException e) {
                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
            }
        }
        req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
    }

    private void downloadAction(HttpServletRequest req, HttpServletResponse resp, String export) throws IOException {
        req.setCharacterEncoding(DownloadConstants.DEFAULT_CHARACTER_ENCODING);
        resp.setCharacterEncoding(DownloadConstants.DEFAULT_CHARACTER_ENCODING);
        ServletOutputStream out = resp.getOutputStream();
        resp.addHeader("Content-Disposition", "attachment;filename=" + DownloadConstants.DEFAULT_EXPORT_FILE_NAME);
        resp.setContentType(DownloadConstants.DEFAULT_CONTENT_TYPE);
        StringReader sr = new StringReader(export);
        int i;
        while ((i = sr.read()) != -1) {
            out.write(i);
        }
        out.close();
    }
}
