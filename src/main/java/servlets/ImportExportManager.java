package servlets;

import auxiliaryclasses.ConstantsClass;
import server.controller.XmlUtils;
import server.exportdata.ExportException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImportExportManager {
    private static ImportExportManager ourInstance = new ImportExportManager();
    private XmlUtils xmlUtils = XmlUtils.getInstance();

    public static ImportExportManager getInstance() {
        return ourInstance;
    }

    private ImportExportManager() {
    }

    public void doImport(Part filePart, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fileData;
        InputStream is = filePart.getInputStream();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        fileData = sb.toString();
        if (fileData.contains(ConstantsClass.JOURNAL_CONTAINER_TAG)) {
            String xsl = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNALS_XSL));
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, fileData);
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, null);
            req.setAttribute(ConstantsClass.XSL_JOURNAL_CONTAINER_PARAMETER, xsl);
            req.getRequestDispatcher(ConstantsClass.JOURNALS_XSL_ADDRESS).forward(req, resp);
        } else if (fileData.contains(ConstantsClass.JOURNAL_TAG)) {
            String xsl = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.TASKS_XSL));
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, fileData);
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, null);
            req.setAttribute(ConstantsClass.XSL_JOURNAL_PARAMETER, xsl);
            req.getRequestDispatcher(ConstantsClass.TASKS_XSL_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.INCORRECT_FILE_CONTENT);
        }
    }

    public List<Integer> createIDList(String[] checkBoxes) {
        List<Integer> IDs = new LinkedList<>();
        for (String checkBox : checkBoxes) {
            IDs.add(Integer.parseInt(checkBox));
        }
        return Collections.unmodifiableList(IDs);
    }
}
