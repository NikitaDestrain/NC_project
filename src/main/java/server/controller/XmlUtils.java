package server.controller;

import server.model.JournalNamesContainer;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlUtils {
    private static XmlUtils ourInstance = new XmlUtils();
    private static final String SUCCESSFULLY_VALIDATION = "Successful validation!";
    private static final String UNSUCCESSFULLY_VALIDATION = "Validation error: ";

    public static XmlUtils getInstance() {
        return ourInstance;
    }

    private XmlUtils() {
    }

    public String marshalToXmlString(Class className, Object object) throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(className);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, sw);
        return sw.toString();
    }

    public String parseXmlToString(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        return sb.toString();
    }

    public boolean compareWithXsd(String fileXML, String fileXSD) throws Exception {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new ByteArrayInputStream(fileXSD.getBytes(StandardCharsets.UTF_8))));
            Validator valid = schema.newValidator();
            valid.validate(new StreamSource(new ByteArrayInputStream(fileXML.getBytes(StandardCharsets.UTF_8))));
            System.out.println(SUCCESSFULLY_VALIDATION);
            return true;
        } catch (Exception e) {
            throw new Exception(UNSUCCESSFULLY_VALIDATION + e.getMessage());
        }
    }

    public void writeNames(JournalNamesContainer container, String path) throws Exception {
        JAXBContext context = JAXBContext.newInstance(JournalNamesContainer.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        OutputStream out = new FileOutputStream(path);
        marshaller.marshal(container, out);
    }
}
