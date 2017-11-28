package model;

import java.io.Serializable;
import java.util.*;

public class Journal implements Serializable {
    private Map<Integer, Task> tasks;

    public Journal(){
        tasks = new HashMap<Integer, Task>();
    }

    public Journal(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(),task);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void setTask(int id, Task task) {
        tasks.remove(id);
        tasks.put(task.getId(), task);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(new LinkedList<Task>(tasks.values()));//todo unmodifiable list DONE
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
