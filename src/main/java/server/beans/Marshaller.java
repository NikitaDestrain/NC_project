package server.beans;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
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
import java.nio.charset.StandardCharsets;

public class Marshaller {

    private static Marshaller instance;

    private Marshaller() {}

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
}
