package servlets;

import auxiliaryclasses.ConstantsClass;
import server.controller.XmlUtils;

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

public class ImportManager {
    private static ImportManager ourInstance = new ImportManager();
    private XmlUtils xmlUtils = XmlUtils.getInstance();

    public static ImportManager getInstance() {
        return ourInstance;
    }

    private ImportManager() {
    }

    public void doImport(Part filePart, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fileData = getStringRepresentation(filePart);
        String xsd = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.LAB4_XSD_FILE)).replaceAll("[^\\x20-\\x7e]", "");
        boolean b;
        try {
            b = xmlUtils.compareWithXsd(fileData, xsd);
        } catch (Exception e) {
            throw new IOException(ConstantsClass.ERROR_XSD_COMPARING);
        }
        if (b) {
            String xsl = xmlUtils.parseXmlToString(req.getServletContext().getRealPath(ConstantsClass.JOURNALS_XSL));
            req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, fileData);
            req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, null);
            req.setAttribute(ConstantsClass.XSL_JOURNAL_CONTAINER_PARAMETER, xsl);
            req.getRequestDispatcher(ConstantsClass.JOURNALS_XSL_ADDRESS).forward(req, resp);
        } else {
            resp.getWriter().print(ConstantsClass.INCORRECT_FILE_CONTENT);
        }
    }

    private String getStringRepresentation(Part filePart) throws IOException {
        InputStream is = filePart.getInputStream();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Integer> createIDList(String[] checkBoxes) {
        List<Integer> IDs = new LinkedList<>();
        for (String checkBox : checkBoxes) {
            IDs.add(Integer.parseInt(checkBox));
        }
        return Collections.unmodifiableList(IDs);
    }
}
