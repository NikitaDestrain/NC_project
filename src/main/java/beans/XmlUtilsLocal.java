package beans;

import javax.ejb.Local;
import javax.xml.bind.JAXBException;
import java.io.IOException;

@Local
public interface XmlUtilsLocal {
    String marshalToString(Class className, Object object) throws JAXBException;
    String parseXmlToString(String path) throws IOException;
}
