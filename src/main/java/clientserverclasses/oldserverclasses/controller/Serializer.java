package clientserverclasses.oldserverclasses.controller;

import server.model.Journal;


public interface Serializer {
    void writeJournal(Journal journal, String path) throws Exception;

    Journal readJournal(String path) throws Exception;
}
