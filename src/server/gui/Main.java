package gui;

import client.commandprocessor.temporary.ParserXml;

import org.xml.sax.SAXException;
import server.factories.TaskFactory;
import server.model.Task;
import server.model.TaskStatus;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {

    /*    if (ParserProperties.getInstance() == null) {

            JOptionPane.showMessageDialog(null, "Config file not found or damaged!", "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    Journal journal = new ObjectSerializer().readJournal(ParserProperties.getInstance().getProperties("PATH_TO_JOURNAL"));//todo константы стоит выносить в public static final переменные
                    if (journal == null) {
                        IDGenerator.getInstance();
                        JOptionPane.showMessageDialog(null, "Incorrect journal in file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
                    } else
                        IDGenerator.getInstance(journal.getMaxId());
                    new MainForm().setJournal(journal);
                } catch (IllegalPropertyException ex) {
                    JOptionPane.showMessageDialog(null, "Illegal value of property",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Could not load journal from file. You may create a new one", "Error", JOptionPane.ERROR_MESSAGE);
                    IDGenerator.getInstance();
                    new MainForm().setJournal(null);
                }

            });
        }*/

        ParserXml parser = new ParserXml();

        try {
            parser.readXMLCommand(new FileInputStream("src/client.commandprocessor/test_xml_AddTask.xml")); //чтение из потока


            Task t = TaskFactory.createTask("name", TaskStatus.Cancelled, "description", Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
            parser.creatXMLCommandAddTask(new FileOutputStream("src/client.commandprocessor/test_xml_create.xml"), t);
        }
         catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
