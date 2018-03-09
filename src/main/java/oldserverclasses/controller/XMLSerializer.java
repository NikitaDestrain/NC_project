package oldserverclasses.controller;

import server.model.Journal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class XMLSerializer implements Serializer {
    @Override
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

    @Override
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
}
