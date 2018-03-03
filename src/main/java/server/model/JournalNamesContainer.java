package server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "journalNames")
@XmlAccessorType(XmlAccessType.FIELD)
public class JournalNamesContainer {
    @XmlElement(name = "name")
    private List<String> names;

    public JournalNamesContainer(List<String> names) {
        this.names = names;
    }

    public JournalNamesContainer() {
        names = new LinkedList<>();
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
