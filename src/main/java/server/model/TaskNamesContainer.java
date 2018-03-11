package server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "taskNames")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskNamesContainer {
    @XmlElement(name = "name")
    private List<String> names;

    public TaskNamesContainer(List<String> names) {
        this.names = names;
    }

    public TaskNamesContainer() {
        names = new LinkedList<>();
    }

    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void addName(String name) {
        names.add(name);
    }

    public void deleteName(String name) {
        names.remove(name);
    }

    public boolean isContain(String name) {
        for (String string : getNames()) {
            if (string.equals(name))
                return true;
        }
        return false;
    }

    public void editName(String oldName, String newName) {
        names.remove(oldName);
        names.add(newName);
    }
}
