package server.beans;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import server.importdata.StoreConstants;
import server.importdata.StoreException;
import server.importdata.StoreItem;
import server.model.Journal;
import server.model.JournalContainer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Marshaller {

    private static Marshaller instance;

    private Marshaller() {}

    public static Marshaller getInstance() {
        if (instance == null)
            instance = new Marshaller();
        return instance;
    }

    public StoreItem unmarshal(String path) throws StoreException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new FileInputStream(path));

            if ((document.getElementsByTagName(StoreConstants.JOURNAL)).getLength() > 0) {
                Journal journal = unmarshalJournal(path);
                return new StoreItem(StoreConstants.TASK, journal);
            } else if (document.getElementsByTagName(StoreConstants.JOURNAL_CONTAINER).getLength() > 0) {
                JournalContainer container = unmarshalJournalContainer(path);
                return new StoreItem(StoreConstants.JOURNAL, container);
            } else {
                throw new StoreException("Unknown XML!");
            }
        } catch (ParserConfigurationException | SAXException | IOException | JAXBException e) {
            throw new StoreException("Unable to parse XML!");
        }
    }

    private Journal unmarshalJournal(String path) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            InputStream in = new FileInputStream(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Journal) unmarshaller.unmarshal(in);
        } catch (JAXBException | FileNotFoundException e) {
            throw new JAXBException(e.getMessage());
        }
    }

    private JournalContainer unmarshalJournalContainer(String path) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            InputStream in = new FileInputStream(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (JournalContainer) unmarshaller.unmarshal(in);
        } catch (JAXBException | FileNotFoundException e) {
            throw new JAXBException(e.getMessage());
        }
    }
}
