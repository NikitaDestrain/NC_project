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

    public void setTask(int id, Task task) {//todo не понятна логика метода. Он получает на вход id и таску. Удаляет из журнала таску с указанным id
        // и добавляет в журнал новую таску (у которой в общем случае может быть такой же, а может быть и другой id)
        // Для какого сценария может потребоваться такая логика?
        tasks.remove(id);
        tasks.put(task.getId(), task);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public List<Task> getTasks() {
        LinkedList<Task> list = new LinkedList<Task>(tasks.values());
        list.sort(new TaskComparator());
        return Collections.unmodifiableList(list);
    }

    public int getMaxId(){
        int res = 0;
        for (Integer key: tasks.keySet())
            if(res < key)
                res = key;
        return res;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getClass().getSimpleName() + " (" + this.tasks + ")");
        return stringBuffer.toString();
    }
}
