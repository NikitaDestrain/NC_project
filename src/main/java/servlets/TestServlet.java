package servlets;

import server.beans.EIBeanLocal;
import server.exportdata.config.ExportConfigParser;
import server.exportdata.config.IllegalPropertyException;
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
public class TestServlet extends HttpServlet {

//    @EJB
//    private EIBeanLocal XmlUtilsImpl;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ExportConfigParser configParser = ExportConfigParser.getInstance(req.getServletContext().getRealPath("properties/exportconfig.properties"));
            resp.getWriter().print(configParser.getProperty("TASK"));
        } catch (IOException e) {
            resp.getWriter().print(e.getMessage());
        }
    }


    @Override
    public void init() throws ServletException {
    }
}
