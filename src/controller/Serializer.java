package controller;

import model.Journal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {
    public void writeJournal (Journal journal) throws IOException;
    public Journal readJournal () throws IOException;
}
