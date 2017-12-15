package server.controller;

import server.model.Journal;
import server.properties.ParserProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class XMLSerializer implements Serializer {
    @Override
    public void writeJournal(Journal journal, String path) throws Exception {
        try{
            JAXBContext context = JAXBContext.newInstance(Journal.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new FileOutputStream(path);
            marshaller.marshal(journal, out);
        } catch (JAXBException e) {
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
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
        } catch (JAXBException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return journal;
    }
}
