package servlets;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.DownloadConstants;
import server.controller.XmlUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
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

    public void downloadAction(HttpServletRequest req, HttpServletResponse resp, String export) throws IOException {
        req.setCharacterEncoding(DownloadConstants.DEFAULT_CHARACTER_ENCODING);
        resp.setCharacterEncoding(DownloadConstants.DEFAULT_CHARACTER_ENCODING);
        ServletOutputStream out = resp.getOutputStream();
        resp.addHeader("Content-Disposition", "attachment;filename=" + DownloadConstants.DEFAULT_EXPORT_FILE_NAME);
        resp.setContentType(DownloadConstants.DEFAULT_CONTENT_TYPE);
        StringReader sr = new StringReader(export);
        int i;
        while ((i = sr.read()) != -1) {
            out.write(i);
        }
        out.close();
    }
}
