package model;

import java.util.List;

public class IDGenerator {
    List<Task> taskList;
    public int createId() {
        return taskList == null ? -1 : taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    private boolean existID(int id) {
        return taskList != null && id < taskList.size();
    }

    public void updateID(int id) {
        if (existID(id)) {
            for (int i = id; i < taskList.size(); i++) {
                taskList.get(i).setId(i);
            }
        }
    }
}
