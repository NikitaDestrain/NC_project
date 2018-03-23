package beans;

import javax.ejb.Local;
import javax.xml.bind.JAXBException;

@Local
public interface XmlUtilsLocal {
    String marshalToString(Class className, Object object) throws JAXBException;
}
