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
        tasks.sort(new TaskComparator());
    }

    /*
    public void addTask(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, int id) {
        Task task = new Task(name, status, description, notificationDate, plannedDate);//todo tasks creation is not journal responsibility FACTORY TASKS
        tasks.add(task);
        tasks.sort(new TaskComparator());
    }*/

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void setTask(int id, Task task) {
        tasks.set(id, task);
        //todo simple replace? DONE
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);//todo unmodifiable list DONE
    }

    public int getMaxId(){
        int res = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getId() > res)
                res = tasks.get(i).getId();
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getClass().getSimpleName() + " (" + this.tasks + ")");
        return stringBuffer.toString();
    }
}
