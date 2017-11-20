package model;

import java.io.Serializable;
import java.util.*;

public class Journal implements Serializable {
    private List<Task> tasks;
    private IDGenerator generator = new IDGenerator();

    public Journal(){
        tasks = new LinkedList<>();
        generator.setTaskList(tasks);
    }

    public Journal(List<Task> tasks) {
        this.tasks = tasks;
        generator.setTaskList(tasks);
    }

    public IDGenerator getGenerator() {
        return generator;
    }

    public void addTask(Task task) {
        task.setId(generator.createId());
        tasks.add(task);
    }

    public void addTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate) {
        Task task = new Task(name, status, description, notificationDate, plannedDate);
        task.setId(generator.createId());
        tasks.add(task);
        tasks.sort(new TaskComparator());
    }

    public void removeTask(int id) {
        tasks.remove(id);
        //generator.updateID(id);
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
