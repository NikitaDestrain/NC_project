package model;

import java.util.List;

public class IDGenerator {
    public static int createId() {
        List<Task> list = new Journal().getTasks();
        return list == null ? -1 : list.size() + 1;
    }
}
