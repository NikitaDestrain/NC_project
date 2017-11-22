package model;

import java.io.Serializable;
import java.util.*;

public class Journal implements Serializable {
    private List<Task> tasks;
    private IDGenerator generator = new IDGenerator();//todo IDGenerator must be single in whole system

    public Journal(){
        tasks = new LinkedList<>();
        generator.setTaskList(tasks);
    }

    public Journal(List<Task> tasks) {
        this.tasks = tasks;
        generator.setTaskList(tasks);
    }

    //todo not needed?
    public IDGenerator getGenerator() {
        return generator;
    }

    public void addTask(Task task) {
        task.setId(generator.createId());
        tasks.add(task);
    }

    public void addTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate) {
        Task task = new Task(name, status, description, notificationDate, plannedDate);//todo tasks creation is not journal responsibility FACTORY TASKS
        task.setId(generator.createId());
        tasks.add(task);
        tasks.sort(new TaskComparator());
    }

    public void removeTask(int id) {
        tasks.remove(id);
        //generator.updateID(id);
    }

    public void setTask(int id, Task task) {
        tasks.set(id,task);
        //todo simple replace? DONE
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);//todo unmodifiable list DONE
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getClass().getSimpleName() + " (" + this.tasks + ")");
        return stringBuffer.toString();
    }
}
