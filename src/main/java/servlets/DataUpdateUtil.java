package servlets;

import auxiliaryclasses.ConstantsClass;
import server.controller.Controller;
import server.controller.XmlUtils;
import server.exceptions.ControllerActionException;
import server.model.Journal;
import server.model.JournalContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class DataUpdateUtil {
    private static DataUpdateUtil ourInstance;
    private XmlUtils xmlUtils;
    private Controller controller;

    public static DataUpdateUtil getInstance() throws ControllerActionException {
        if (ourInstance == null) ourInstance = new DataUpdateUtil();
        return ourInstance;
    }

    private DataUpdateUtil() throws ControllerActionException {
        controller = Controller.getInstance();
        xmlUtils = XmlUtils.getInstance();
    }

    public void updateJournals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JournalContainer journals = controller.getJournals();
        try {
            String updatedJournals = xmlUtils.marshalToXmlString(journals.getClass(), journals);
            if (updatedJournals != null) {
                req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, updatedJournals);
                req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
            }
        } catch (JAXBException e) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
        }
    }

    public void updateSortedJournals(HttpServletRequest req, HttpServletResponse resp, String sortedJournals) throws ServletException, IOException {
        //todo то же самое что с апдейт таскс
        if (sortedJournals != null) {
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, sortedJournals);
            req.setAttribute(ConstantsClass.IS_SORTED, Boolean.TRUE);
            req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
    }

    public void updateTasks(HttpServletRequest req, HttpServletResponse resp, Journal currentJournal) throws ServletException, IOException {
        Journal journal = controller.getTasks(currentJournal.getId());
        try {
            String updatedJournal = xmlUtils.marshalToXmlString(journal.getClass(), journal);
            if (updatedJournal != null) {
                req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, updatedJournal);
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
            }
        } catch (JAXBException e) {
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
        }
    }

    public void updateSortedTasks(HttpServletRequest req, HttpServletResponse resp, String sortedTasks) throws ServletException, IOException {
        if (sortedTasks != null) {
            //todo здесь нужно взять данные из хмл, так как модель не меняется, чтобы не затереть при паттерне
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, sortedTasks);
            req.setAttribute(ConstantsClass.IS_SORTED, Boolean.TRUE);
            req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
    }
}
