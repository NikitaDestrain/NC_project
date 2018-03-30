package server.beans;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import server.importdata.StoreException;
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

    public Object unmarshal(String xml) throws StoreException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

            if ((document.getElementsByTagName("journal")).getLength() > 0) {
                return unmarshalJournal(xml);
            } else if (document.getElementsByTagName("journalContainer").getLength() > 0) {
                return unmarshalJournalContainer(xml);
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
            StringReader stringReader = new StringReader(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Journal) unmarshaller.unmarshal(stringReader);
        } catch (JAXBException e) {
            throw new JAXBException(e.getMessage());
        }
    }

    private JournalContainer unmarshalJournalContainer(String path) throws JAXBException {
        try {
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            StringReader stringReader = new StringReader(path);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (JournalContainer) unmarshaller.unmarshal(stringReader);
        } catch (JAXBException e) {
            throw new JAXBException(e.getMessage());
        }
    }
}
