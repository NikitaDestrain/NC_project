package servlets;

import auxiliaryclasses.ConstantsClass;
import server.beans.EIBeanLocal;
import server.exceptions.ControllerActionException;
import server.importdata.StoreException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(ConstantsClass.IMPORT_SERVLET_ADDRESS)
@MultipartConfig
public class ImportServlet extends HttpServlet {

    private ImportManager importExportManager = ImportManager.getInstance();
    private DataUpdateUtil updateUtil;

    @EJB
    EIBeanLocal ExportImportBean;

    @Override
    public void init() throws ServletException {
        try {
            updateUtil = DataUpdateUtil.getInstance();
        } catch (ControllerActionException e) {
            throw new ServletException(e.getMessage());
        }
    }

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
        String journalStrategy = req.getParameter(ConstantsClass.JOURNAL_STRATEGY);
        String taskStrategy = req.getParameter(ConstantsClass.TASK_STRATEGY);
        String xml = req.getSession().getAttribute(ConstantsClass.JOURNAL_PARAMETER)==null?
                (String) req.getSession().getAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER):
                (String) req.getSession().getAttribute(ConstantsClass.JOURNAL_PARAMETER);

        try {
            ExportImportBean.importData(xml, journalStrategy, taskStrategy);
            updateUtil.updateJournals(req, resp);
        } catch (StoreException e) {
            resp.getWriter().print(e.getMessage());
        }
    }
}
