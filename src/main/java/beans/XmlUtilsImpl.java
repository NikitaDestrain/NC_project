package beans;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

@Local(XmlUtilsLocal.class)
@Stateless
public class XmlUtilsImpl implements XmlUtilsLocal {

    public XmlUtilsImpl() {}

    @Override
    public String marshalToString(Class className, Object object) throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(className);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, sw);
        return sw.toString();
    }
}
