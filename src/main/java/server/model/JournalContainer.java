package server.model;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name = "journalContainer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Journal.class})
public class JournalContainer {
    @XmlElement(name = "journals")
    private Map<Integer, Journal> journals;

    public JournalContainer() {
        journals = new LinkedHashMap<>();
    }

    public void addJournal(Journal journal) {
        journals.put(journal.getId(), journal);
    }

    public void removeJournal(Journal journal) {
        journals.remove(journal.getId());
    }

    public void removeJournal(int id) {
        journals.remove(id);
    }

    public Journal getJournal(int id) {
        return journals.get(id);
    }

    public Journal getJournal(String name) {
        for (Journal journal : getJournals())
            if ((journal.getName()).equals(name))
                return journal;
        return null;
    }

    public List<Journal> getJournals() {
        LinkedList<Journal> list = new LinkedList<>(journals.values());
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        return "JournalContainer{" +
                "journals=" + journals +
                '}';
    }
}
