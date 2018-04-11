package server.beans;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import server.exceptions.MarshallerException;
import server.importdata.StoreConstants;
import server.importdata.StoreException;
import server.importdata.StoreItem;
import server.model.Journal;
import server.model.JournalContainer;
import server.model.Task;
import server.model.TaskStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.List;

public class Marshaller {

    private static Marshaller instance;

    private Marshaller() {
    }

    public static Marshaller getInstance() {
        if (instance == null)
            instance = new Marshaller();
        return instance;
    }

    public StoreItem unmarshal(String xml) throws StoreException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))));

            if ((document.getElementsByTagName(StoreConstants.JOURNAL)).getLength() > 0) {
                Journal journal = unmarshalJournal(xml);
                return new StoreItem(journal);
            } else if (document.getElementsByTagName(StoreConstants.JOURNAL_CONTAINER).getLength() > 0) {
                JournalContainer container = unmarshalJournalContainer(xml);
                return new StoreItem(container);
            } else {
                throw new StoreException("Unknown XML!");
            }
        } catch (ParserConfigurationException | SAXException | IOException | JAXBException e) {
            throw new StoreException("Unable to parse XML!");
        }
    }

    private Journal unmarshalJournal(String xml) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            StringReader reader = new StringReader(xml);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Journal) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new JAXBException(e.getMessage());
        }
    }

    private JournalContainer unmarshalJournalContainer(String xml) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            StringReader reader = new StringReader(xml);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (JournalContainer) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new JAXBException(e.getMessage());
        }
    }

    public String marshalToXMLString(List<Journal> journals, List<Task> tasks) throws MarshallerException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            createXMLSchema(doc, journals, tasks);
            return createStringFromDocument(doc);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new MarshallerException();
        }
    }

    private void createXMLSchema(Document doc, List<Journal> journals, List<Task> tasks) {
        Element rootElement = doc.createElement("container");
        doc.appendChild(rootElement);

        Element journalsElement = doc.createElement("journals");
        rootElement.appendChild(journalsElement);
        createJournalElements(doc, journalsElement, journals);

        Element tasksElement = doc.createElement("tasks");
        rootElement.appendChild(tasksElement);
        createTaskElements(doc, tasksElement, tasks);
    }

    private void createTaskElements(Document doc, Element rootElement, List<Task> tasks) {
        if (tasks == null)
            return;
        for (Task task : tasks) {
            rootElement.appendChild(createTaskNode(doc, task.getId(), task.getName(), task.getStatus(),
                    task.getDescription(), task.getNotificationDate(), task.getPlannedDate(), task.getUploadDate(),
                    task.getChangeDate(), task.getJournalId()));
        }
    }

    private Node createTaskNode(Document doc, Integer id, String name, TaskStatus status, String description,
                                Date notificationDate, Date plannedDate, Date uploadDate, Date changeDate,
                                Integer journalId) {
        Element task = doc.createElement("task");
        task.appendChild(createElementWithValue(doc, "id", id.toString()));
        task.appendChild(createElementWithValue(doc, "name", name));
        task.appendChild(createElementWithValue(doc, "status", status.toString()));
        task.appendChild(createElementWithValue(doc, "description", description));
        task.appendChild(createElementWithValue(doc, "notificationDate", notificationDate.toString()));
        task.appendChild(createElementWithValue(doc, "plannedDate", plannedDate.toString()));
        task.appendChild(createElementWithValue(doc, "uploadDate", uploadDate.toString()));
        task.appendChild(createElementWithValue(doc, "changeDate", changeDate.toString()));
        task.appendChild(createElementWithValue(doc, "journalId", journalId.toString()));
        return task;
    }

    private void createJournalElements(Document doc, Element rootElement, List<Journal> journals) {
        if (journals == null)
            return;
        for (Journal journal : journals) {
            rootElement.appendChild(createJournalNode(doc, journal.getId(), journal.getName(), journal.getDescription()));
        }
    }

    private Node createJournalNode(Document doc, Integer id, String name, String description) {
        Element journal = doc.createElement("journal");
        journal.appendChild(createElementWithValue(doc, "id", id.toString()));
        journal.appendChild(createElementWithValue(doc, "name", name));
        journal.appendChild(createElementWithValue(doc, "description", description));
        return journal;
    }

    private Node createElementWithValue(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private String createStringFromDocument(Document doc) throws TransformerException {
        try (StringWriter stringWriter = new StringWriter()) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new TransformerException(e.getMessage());
        }
    }
}
