package model;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    //вернет 1, если у задачи 1 дата идет позже, чем у задачи 2
    public int compare(Task task1, Task task2) {
        if(task1.compareTo(task2) > 0)
            return 1;
        if(task1.compareTo(task2) < 0)
            return -1;
        return 0;
    }
}
