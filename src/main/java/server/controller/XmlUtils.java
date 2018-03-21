package server.controller;

import server.model.Journal;
import server.model.JournalContainer;
import server.model.JournalNamesContainer;
import server.model.Task;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
    private static final String TASK_CLASS = "server.model.Task";
    private static final String JOURNAL_CLASS = "server.model.Journal";

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
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public boolean compareWithXsd(String nameFileXML, String nameFileXSD) throws Exception {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(nameFileXSD));
            Validator valid = schema.newValidator();
            valid.validate(new StreamSource(new ByteArrayInputStream(nameFileXML.getBytes(StandardCharsets.UTF_8))));
            System.out.println(SUCCESSFULLY_VALIDATION);
            return true;
        } catch (Exception e) {
            throw new Exception(UNSUCCESSFULLY_VALIDATION + e.getMessage());
        }
    }

    public void writeJournal(Journal journal, String path) throws Exception {
        try {
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new FileOutputStream(path);
            marshaller.marshal(journal, out);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public Journal readJournal(String path) throws Exception {
        Journal journal;
        try {
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            InputStream in = new FileInputStream(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            journal = (Journal) unmarshaller.unmarshal(in);
        } catch (Exception e) {
            throw new Exception();
        }
        return journal;
    }

    public void writeJournalContainer(JournalContainer container, String path) throws Exception {
        try {
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new FileOutputStream(path);
            marshaller.marshal(container, out);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public JournalContainer readJournalContainer(String path) throws Exception {
        JournalContainer journalContainer;
        try {
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            InputStream in = new FileInputStream(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            journalContainer = (JournalContainer) unmarshaller.unmarshal(in);
        } catch (Exception e) {
            throw new Exception();
        }
        return journalContainer;
    }

    public JournalNamesContainer readNames(String path) throws Exception {
        JournalNamesContainer container;
        try {
            JAXBContext context = JAXBContext.newInstance(JournalNamesContainer.class);
            InputStream in = new FileInputStream(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            container = (JournalNamesContainer) unmarshaller.unmarshal(in);
        } catch (Exception e) {
            throw new Exception();
        }
        return container;
    }

    public void writeNames(JournalNamesContainer container, String path) throws Exception {
        try {
            JAXBContext context = JAXBContext.newInstance(JournalNamesContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new FileOutputStream(path);
            marshaller.marshal(container, out);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void writeTask(Task task, String path) throws Exception {
        try {
            JAXBContext context = JAXBContext.newInstance(Task.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new FileOutputStream(path);
            marshaller.marshal(task, out);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public Task readTask(String path) throws Exception {
        Task task;
        try {
            JAXBContext context = JAXBContext.newInstance(Task.class);
            InputStream in = new FileInputStream(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            task = (Task) unmarshaller.unmarshal(in);
        } catch (Exception e) {
            throw new Exception();
        }
        return task;
    }
}
