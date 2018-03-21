package servlets;

import auxiliaryclasses.ConstantsClass;
import server.controller.XmlUtils;
import server.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    Task task1 = new Task(0, "1", TaskStatus.Overdue, "desc1", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
            new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 0);
    Task task2 = new Task(1, "2", TaskStatus.Overdue, "desc2", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
            new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 1);
    Task task3 = new Task(2, "3", TaskStatus.Overdue, "desc3", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
            new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 1);
    Journal journal1 = new Journal();
    Journal journal2 = new Journal();
    JournalContainer container = new JournalContainer();
    List<String> names = new LinkedList<>();
    JournalNamesContainer namesContainer;

    private XmlUtils xmlUtils = XmlUtils.getInstance();

    private void add() {
        journal1.addTask(task1);
        journal1.addTask(task2);
        journal1.addTask(task3);
        journal1.setName("j1");
        journal1.setId(0);

        journal2.addTask(task2);
        journal2.addTask(task1);
        journal2.setName("j2");
        journal2.setDescription("j1Desc");
        journal2.setId(1);

        container.addJournal(journal1);
        container.addJournal(journal2);

        namesContainer = new JournalNamesContainer(names);

        for (Journal j : container.getJournals()) {
            names.add(j.getName());
        }
    }

    @Override
    public void init() throws ServletException {
        add();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "fromstart":
                req.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.TRUE);
                req.getSession().setAttribute(ConstantsClass.CURRENT_JOURNAL_NAME, journal1.getName());
                String t = null;
                try {
                    t = xmlUtils.marshalToXmlString(Task.class, task1);
                } catch (Exception ex) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
                try {
                    xmlUtils.writeNames(namesContainer,
                            req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE));
                } catch (Exception e) {
                    resp.getWriter().print(ConstantsClass.ERROR_XML_WRITING);
                }
                String n = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.NAMES_XML_FILE));
                req.setAttribute(ConstantsClass.JOURNAL_NAMES, n);
                req.setAttribute(ConstantsClass.CURRENT_TASK_XML, t);
                String xslTask = null;
                xslTask = xmlUtils.parseXmlToString(req.getServletContext().getRealPath("/localbackup/addtask.xsl"));
                req.setAttribute("xslTask", xslTask);
                String xslNames = xmlUtils.parseXmlToString(req.getServletContext().getRealPath("/localbackup/names.xsl"));
                req.setAttribute("xslNames", xslNames);
                req.getRequestDispatcher("/updatetasks4").forward(req, resp);
                break;
            case "addtask":
                resp.getWriter().print("Add task");
        }
    }
}
