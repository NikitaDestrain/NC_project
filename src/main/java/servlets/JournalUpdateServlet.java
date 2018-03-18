package servlets;

import auxiliaryclasses.ConstantsClass;
import server.controller.Controller;
import server.controller.XmlUtils;
import server.exceptions.ControllerActionException;
import server.model.Journal;
import server.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ConstantsClass.JOURNAL_UPDATE_SERVLET_ADDRESS)
public class JournalUpdateServlet extends HttpServlet {
    private Controller controller;
    private DataUpdateUtil updateUtil;
    private XmlUtils xmlUtils = XmlUtils.getInstance();
    private PatternChecker patternChecker = PatternChecker.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.DO_ADD_JOURNAL:
                doAddJournal(req, resp);
                break;
            case ConstantsClass.DO_EDIT_JOURNAL:
                doEditJournal(req, resp);
                break;
        }
    }

    private void doAddJournal(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        try {
            controller = Controller.getInstance();
            updateUtil = DataUpdateUtil.getInstance();
        } catch (ControllerActionException e) {
            //todo обсудить
            e.printStackTrace();
        }
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        User currentUser = (User) req.getSession().getAttribute(ConstantsClass.CURRENT_USER);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);

                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !patternChecker.isLatinChars(name)) {
                    incorrectAddJournal(req, resp, name, description, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !patternChecker.isCorrectDescription(description)) {
                    incorrectAddJournal(req, resp, name, description, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    int userId = currentUser.getId();
                    try {
                        controller.addJournal(name, description, userId);
                        updateUtil.updateJournals(req, resp);
                    } catch (ControllerActionException e) {
                        incorrectAddJournal(req, resp, name, description, e.getMessage());
                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
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
        boolean journalCorrect = false;
        try {
            journalCorrect = xmlUtils.compareWithXsd(
                    req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE),
                    req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XSD_FILE));
        } catch (Exception e) {
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
            req.getRequestDispatcher(ConstantsClass.UPDATE_JOURNALS_ADDRESS).forward(req, resp);
        }
        if (journalCorrect) {
            String j = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
            req.setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.UPDATE_JOURNALS_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }

    private void doEditJournal(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        try {
            controller = Controller.getInstance();
            updateUtil = DataUpdateUtil.getInstance();
        } catch (ControllerActionException e) {
            //todo обсудить
            e.printStackTrace();
        }
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.SAVE:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);
                Journal currentJournal = (Journal) req.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL);
                if (name.length() == 0 || name.length() > ConstantsClass.NAME_FIELD_LENGTH || !patternChecker.isLatinChars(name)) {
                    incorrectEditJournal(req, resp, name, description, ConstantsClass.ERROR_NAME_LENGTH);
                } else if (description.length() > ConstantsClass.DESCRIPTION_FIELD_LENGTH || !patternChecker.isCorrectDescription(description)) {
                    incorrectEditJournal(req, resp, name, description, ConstantsClass.ERROR_DESCRIPTION_LENGTH);
                } else {
                    try {
                        controller.editJournal(currentJournal.getId(), name, description);
                        updateUtil.updateJournals(req, resp);
                    } catch (ControllerActionException e) {
                        incorrectEditJournal(req, resp, name, description, e.getMessage());
                    }
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
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
        boolean journalCorrect = false;
        try {
            journalCorrect = xmlUtils.compareWithXsd(
                    req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE),
                    req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XSD_FILE));
        } catch (Exception e) {
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, e.getMessage());
            req.getRequestDispatcher(ConstantsClass.UPDATE_JOURNALS_ADDRESS).forward(req, resp);
        }
        if (journalCorrect) {
            String j = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNAL_XML_FILE));
            req.setAttribute(ConstantsClass.JOURNAL_PARAMETER, j);
            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
            req.getRequestDispatcher(ConstantsClass.UPDATE_JOURNALS_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.ERROR_XSD_COMPARING);
        }
    }
}
