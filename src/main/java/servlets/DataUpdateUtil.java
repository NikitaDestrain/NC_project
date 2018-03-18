package servlets;

import auxiliaryclasses.ConstantsClass;
import server.controller.Controller;
import server.model.Journal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DataUpdateUtil {
    private static DataUpdateUtil ourInstance = new DataUpdateUtil();
    private Controller controller = Controller.getInstance();

    public static DataUpdateUtil getInstance() {
        return ourInstance;
    }

    private DataUpdateUtil() {
    }

    public void updateJournals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String updatedJournals = controller.getJournals();
        if (updatedJournals != null) {
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, updatedJournals);
            req.getRequestDispatcher(ConstantsClass.JOURNAL_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
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
        String updatedJournal = controller.getTasks(currentJournal.getId());
        if (updatedJournal != null) {
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, updatedJournal);
            req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
        } else
            resp.getWriter().print(ConstantsClass.ERROR_XML_READING);
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
