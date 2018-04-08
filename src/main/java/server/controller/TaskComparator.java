package server.controller;

import server.model.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    /**
     * Compares received tasks by their notification date
     *
     * @param task1
     * @param task2
     * @return 1 if the date of task1 is after the date of task2, otherwise -1. If two dates are equal returns 0
     */
    //вернет 1, если у задачи 1 дата идет позже, чем у задачи 2
    public int compare(Task task1, Task task2) {
        if (task1.compareTo(task2) > 0)
            return 1;
        if (task1.compareTo(task2) < 0)
            return -1;
        return 0;
    }
}
