package server.controller;

import server.model.Journal;

import java.io.IOException;


public interface Serializer {
    void writeJournal (Journal journal, String path) throws Exception;
    Journal readJournal (String path) throws Exception;
}
