package server.controller;

import server.model.Journal;

import java.io.IOException;


public interface Serializer {
    void writeJournal (Journal journal, String path) throws IOException;
    Journal readJournal (String path) throws IOException;
}
