package servlets;

import server.exportdata.config.ExportConfigParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

//    @EJB
//    private EIBeanLocal XmlUtilsImpl;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ExportConfigParser configParser = ExportConfigParser.getInstance(req.getServletContext().getRealPath("properties/exportconfig.properties"));
            resp.getWriter().print(configParser.getPropertyValue("TASK"));
        } catch (IOException e) {
            resp.getWriter().print(e.getMessage());
        }
    }


    @Override
    public void init() throws ServletException {
    }
}
