package server.beans;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import server.exceptions.MarshallerException;
import server.factories.JournalFactory;
import server.factories.TaskFactory;
import server.importdata.StoreConstants;
import server.importdata.StoreException;
import server.importdata.StoreItem;
import server.model.Journal;
import server.model.Task;
import server.model.TaskStatus;
import servlets.CurrentUserContainer;

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
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

public class Marshaller {

    private static Marshaller instance;
    private CurrentUserContainer userContainer = CurrentUserContainer.getInstance();

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

            if ((document.getElementsByTagName(StoreConstants.CONTAINER_TAG)).getLength() > 0) {
                Map<Integer, Journal> parsedJournals = unmarshalJournals(document);
                List<Task> parsedTasks = unmarshalTasks(document);
                addTasksToJournals(parsedJournals, parsedTasks);
                return new StoreItem(Collections.unmodifiableList(new LinkedList<>(parsedJournals.values())),
                        parsedTasks);
            } else
                throw new StoreException(MarshallerConstants.PARSE_ERROR);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new StoreException(MarshallerConstants.PARSE_ERROR);
        }
    }

    private Map<Integer, Journal> unmarshalJournals(Document document) throws StoreException {
        if (userContainer.getUser() == null) {
            throw new StoreException(MarshallerConstants.PARSE_ERROR);
        } else {
            NodeList journals = document.getElementsByTagName(MarshallerConstants.JOURNAL_TAG);
            Map<Integer, Journal> parsedJournals = new HashMap<>();
            Element current;
            Journal journal;
            String name;
            String description = null;
            String id;
            NodeList descriptions;
            for (int i = 0; i < journals.getLength(); i++) {
                current = (Element) journals.item(i);
                descriptions = current.getElementsByTagName(MarshallerConstants.DESCRIPTION_TAG);
                if (descriptions.getLength() != 0) {
                    description = descriptions.item(0).getFirstChild().getNodeValue();
                }
                try {
                    name = current.getElementsByTagName(MarshallerConstants.NAME_TAG).item(0).getFirstChild().getNodeValue();
                    id = current.getElementsByTagName(MarshallerConstants.ID_TAG).item(0).getFirstChild().getNodeValue();
                } catch (NullPointerException e) {
                    throw new StoreException(MarshallerConstants.PARSE_ERROR);
                }
                journal = JournalFactory.createJournal(Integer.parseInt(id), name, description, userContainer.getUser().getId());
                parsedJournals.put(journal.getId(), journal);
            }
            return parsedJournals;
        }
    }

    private List<Task> unmarshalTasks(Document document) throws StoreException {
        NodeList tasks = document.getElementsByTagName(MarshallerConstants.TASK_TAG);
        List<Task> parsedTasks = new LinkedList<>();
        Element current;
        Task task;
        String name;
        String description = null;
        String id;
        String status;
        String notification;
        String planned;
        String upload;
        String change;
        Date notificationDate;
        Date plannedDate;
        Date uploadDate;
        Date changeDate;
        String journalId;
        NodeList descriptions;
        for (int i = 0; i < tasks.getLength(); i++) {
            current = (Element) tasks.item(i);
            descriptions = current.getElementsByTagName(MarshallerConstants.DESCRIPTION_TAG);
            if (descriptions.getLength() != 0) {
                description = descriptions.item(0).getFirstChild() == null ? "" :
                        descriptions.item(0).getFirstChild().getNodeValue();
            }
            try {
                name = current.getElementsByTagName(MarshallerConstants.NAME_TAG).item(0).getFirstChild().getNodeValue();
                //todo vlla а почему вы решили сделать параметры объектов тегами, а не атрибутами?
                id = current.getElementsByTagName(MarshallerConstants.ID_TAG).item(0).getFirstChild().getNodeValue();
                status = current.getElementsByTagName(MarshallerConstants.STATUS_TAG).item(0).getFirstChild().getNodeValue();
                notification = current.getElementsByTagName(MarshallerConstants.NOTIFICATION_DATE_TAG).item(0).getFirstChild().getNodeValue();
                planned = current.getElementsByTagName(MarshallerConstants.PLANNED_DATE_TAG).item(0).getFirstChild().getNodeValue();
                upload = current.getElementsByTagName(MarshallerConstants.UPLOAD_DATE_TAG).item(0).getFirstChild().getNodeValue();
                change = current.getElementsByTagName(MarshallerConstants.CHANGE_DATE_TAG).item(0).getFirstChild().getNodeValue();
                journalId = current.getElementsByTagName(MarshallerConstants.JOURNAL_ID_TAG).item(0).getFirstChild().getNodeValue();
            } catch (NullPointerException e) {
                throw new StoreException(MarshallerConstants.PARSE_ERROR);
            }
            notificationDate = Date.valueOf(notification.substring(0, 10));
            plannedDate = Date.valueOf(planned.substring(0, 10));
            uploadDate = Date.valueOf(upload.substring(0, 10));
            changeDate = Date.valueOf(change.substring(0, 10));

            task = TaskFactory.createTask(Integer.parseInt(id), name, status, description,
                    notificationDate, plannedDate, uploadDate, changeDate, Integer.parseInt(journalId));

            parsedTasks.add(task);
        }
        return parsedTasks;
    }

    private void addTasksToJournals(Map<Integer, Journal> journals, List<Task> tasks) {
        if (journals.size() == 0)
            return;

        Journal j;
        for (Task t : tasks) {
            j = journals.get(t.getJournalId());
            if (j != null) {
                j.addTask(t);
            }
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
        Element rootElement = doc.createElement(MarshallerConstants.ROOT_TAG);
        doc.appendChild(rootElement);

        Element journalsElement = doc.createElement(MarshallerConstants.JOURNALS_TAG);
        rootElement.appendChild(journalsElement);
        createJournalElements(doc, journalsElement, journals);

        Element tasksElement = doc.createElement(MarshallerConstants.TASKS_TAG);
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
        Element task = doc.createElement(MarshallerConstants.TASK_TAG);
        task.appendChild(createElementWithValue(doc, MarshallerConstants.ID_TAG, id.toString()));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.NAME_TAG, name));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.STATUS_TAG, status.toString()));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.DESCRIPTION_TAG, description));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.NOTIFICATION_DATE_TAG, notificationDate.toString()));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.PLANNED_DATE_TAG, plannedDate.toString()));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.UPLOAD_DATE_TAG, uploadDate.toString()));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.CHANGE_DATE_TAG, changeDate.toString()));
        task.appendChild(createElementWithValue(doc, MarshallerConstants.JOURNAL_ID_TAG, journalId.toString()));
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
        Element journal = doc.createElement(MarshallerConstants.JOURNAL_TAG);
        journal.appendChild(createElementWithValue(doc, MarshallerConstants.ID_TAG, id.toString()));
        journal.appendChild(createElementWithValue(doc, MarshallerConstants.NAME_TAG, name));
        journal.appendChild(createElementWithValue(doc, MarshallerConstants.DESCRIPTION_TAG, description));
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
