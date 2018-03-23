package servlets;

import beans.XmlUtilsImpl;
import beans.XmlUtilsLocal;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/test")
public class TestServlet extends HttpServlet{

    private XmlUtilsLocal xmlUtils;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Task task1 = new Task(0, "1", TaskStatus.Overdue, "desc1", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 0);
        Task task2 = new Task(1, "2", TaskStatus.Overdue, "desc2", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 1);
        Task task3 = new Task(2, "3", TaskStatus.Overdue, "desc3", new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()), 1);
        Journal journal1 = new Journal();
        journal1.addTask(task1);
        journal1.addTask(task2);
        journal1.addTask(task3);
        journal1.setName("j1");
        journal1.setId(0);
        try {
            resp.getWriter().print(xmlUtils.marshalToString(Journal.class, journal1));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        xmlUtils = new XmlUtilsImpl();
    }
}
