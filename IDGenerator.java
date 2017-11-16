package model;

import java.util.UUID;

public class IDGenerator {
    public static UUID createId() {
        return UUID.randomUUID();
    }
}
