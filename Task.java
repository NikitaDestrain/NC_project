package model;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Task implements Comparable<Task>, Serializable
{
    private String name;
    private String status;
    private String description;
    private Date notificationDate;
    private Date plannedDate;
    private static UUID id = IDGenerator.createId();

    public Task(String name, String status, String description, Date notificationDate, Date plannedDate) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.notificationDate = notificationDate;
        this.plannedDate = plannedDate;
    }

    public void setTask(Task task)
    {
        this.name = task.getName();
        this.status = task.getStatus();
        this.description = task.getDescription();
        this.notificationDate = task.getNotificationDate();
        this.plannedDate = task.getPlannedDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public Date getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(Date plannedDate) {
        this.plannedDate = plannedDate;
    }

    public static UUID getId() {
        return id;
    }

    //вернет -1 если у задачи переданной в кач-ве аргумента дата идет позже
    @Override
    public int compareTo(Task task) {
        if(notificationDate.compareTo(task.getNotificationDate()) < 0)
            return -1;
        if(notificationDate.compareTo(task.getNotificationDate()) > 0)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getClass().getSimpleName() + " (" + this.getName() + ", " + this.getStatus() + this.getDescription());
        stringBuffer.append(", " + this.getNotificationDate() + ", " + this.getPlannedDate() + ")");
        return stringBuffer.toString();
    }
}
