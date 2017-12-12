package server.model;

import java.io.Serializable;
import java.util.*;

public class Journal implements Serializable {
    private Map<Integer, Task> tasks;

    /**
     * Creates new Journal object and instantiates new {@code HashMap} of tasks
     */
    public Journal(){
        tasks = new HashMap<>();
    }

    /**
     * Puts the task by its id to the map
     * @param task
     */

    public void addTask(Task task) {
        tasks.put(task.getId(),task);
    }

    /**
     * Removes the task from map by its id
     * @param id
     */

    public void removeTask(int id) {
        tasks.remove(id);
    }

    /**
     * Gets the task from map by its id
     */

    public Task getTask(int id) {
        return tasks.get(id);
    }

    /**
     * Gets the list of tasks from this journal
     * @return {@code LinkedList} object that contains all task from the map
     */

    public List<Task> getTasks() {
        LinkedList<Task> list = new LinkedList<>(tasks.values());
        list.sort(new TaskComparator());
        return Collections.unmodifiableList(list);
    }

    /**
     * Gets the max value of key from the map
     */

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
