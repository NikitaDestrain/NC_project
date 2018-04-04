package servlets;

import auxiliaryclasses.ConstantsClass;
import server.beans.EIBeanLocal;
import server.beans.ExportImportBean;
import server.importdata.StoreException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ConstantsClass.IMPORT_SERVLET_ADDRESS)
public class ImportServlet extends HttpServlet {

    @EJB
    EIBeanLocal ExportImportBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.CHOOSE_STRATEGY:
                performStrategy(req, resp);
                break;
        }
    }

    private void performStrategy(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String strategy = req.getParameter(ConstantsClass.USERNUMBER);
        String xml = req.getSession().getAttribute(ConstantsClass.JOURNAL_PARAMETER)==null?
                (String) req.getSession().getAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER):
                (String) req.getSession().getAttribute(ConstantsClass.JOURNAL_PARAMETER);
        resp.getWriter().println(strategy);
        resp.getWriter().println(xml);
//        try {
//            ExportImportBean.importData(xml, strategy);
//        } catch (StoreException e) {
//            resp.getWriter().print(e.getMessage());
//        }
    }
}
