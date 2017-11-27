package controller;

import controller.SerializeDeserialize;

import java.io.Serializable;
import java.util.List;

public class IDGenerator{
    private int nextId = 0;

    public IDGenerator(int nextId) {
        this.nextId = nextId;
    }

    public synchronized int createId() {
        return nextId++;
    }
}
