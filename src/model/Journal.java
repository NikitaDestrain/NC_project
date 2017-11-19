package model;

import java.io.Serializable;
import java.util.*;

public class Journal implements Serializable {
    private List<Task> tasks;

    public Journal(){
        tasks = new LinkedList<>();
    }

    public Journal(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate) {
        tasks.add(new Task(name, status, description, notificationDate, plannedDate));
        tasks.sort(new TaskComparator());
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void setTask(int id, Task task) {
        tasks.get(id).setTask(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getClass().getSimpleName() + " (" + this.tasks + ")");
        return stringBuffer.toString();
    }
}
