package server.model;


import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/** The class serves to store the object "task" with server.exceptions.properties
 * <b>name</b>, <b>status</b>, <b>description</b>, <b>notificationDate</b>,  <b>plannedDate</b>, <b>id</b>.
 */
@XmlType(propOrder = {"id", "name", "description", "status", "notificationDate", "plannedDate"}, name = "Task")
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Comparable<Task>, Serializable {
    /** Task name */
    private String name;
    /** Task status @see TaskStatus */
    private TaskStatus status;
    /** Task description */
    private String description;
    /** Notification date*/
    private Date notificationDate;
    /** Planned date*/
    private Date plannedDate;
    /** unique identificator */
    private int id;

    public Task() {}

    /**
     * Creates a new object with given values
     * @param name
     * @param status
     * @param description
     * @param notificationDate
     * @param plannedDate
     * @param id
     */

    public Task(String name, TaskStatus status, String description, Date notificationDate, Date plannedDate, int id) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.notificationDate = notificationDate;
        this.plannedDate = plannedDate;
        this.id = id;
    }

    /**
     * Changes the current task to a new task.
     * values are set:
     * <br><b>name</b>
     * <br><b>status</b>,
     * <br><b>description</b>,
     * <br><b>notificationDate</b>,
     * <br><b>plannedDate</b>.
     * @param task new task
     */
    public void setTask(Task task) {
        this.name = task.getName();
        this.status = task.getStatus();
        this.description = task.getDescription();
        this.notificationDate = task.getNotificationDate();
        this.plannedDate = task.getPlannedDate();
    }

    /**
     * Return field value   <b>name<b/>
     * @return task name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the task field value <b>name<b/>
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the current  value of the task field <b>status<b/>
     * @see TaskStatus
     * @return {@linkplain TaskStatus } status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Changes the current <b>status</b>,  of the task
     * @param status new status from the class {@linkplain TaskStatus}
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Returns the field value <b>description<b/>
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifies the current task field value  <b>description<b/>
     * @param description  new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the field value <b>notificationDate<b/>
     * @return task notification date
     */
    public Date getNotificationDate() {
        return notificationDate;
    }

    /**
     * Set the value of the field <b>notificationDate<b/>
     * @param notificationDate the new task notification time
     */
    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    /**
     * Returns the field value <b>plannedDate<b/>
     * @return planned task time
     */
    public Date getPlannedDate() {
        return plannedDate;
    }

    /**
     * Set the value of the field <b>plannedDate<b/>
     * @param plannedDate new planned task time
     */
    public void setPlannedDate(Date plannedDate) {
        this.plannedDate = plannedDate;
    }

    /**
     * Returns the field value <b>id</b>
     * @return unique identificator
     */
    public int getId() {
        return id;
    }

    //вернет -1 если у задачи переданной в кач-ве аргумента дата идет позже

    /**
     * Method allows you to compare two tasks by the time of notification
     * @param task object of the task with which the object that called the method is compared
     * @return <b>-1</b> if notification the transferred task object > than notificaton time of this task notification time.
     * Else <b>1</b>
     */
    @Override
    public int compareTo(Task task) {
        if(notificationDate.compareTo(task.getNotificationDate()) < 0)
            return -1;
        if(notificationDate.compareTo(task.getNotificationDate()) > 0)
            return 1;
        return 0;
    }

    /**
     * Method that allows you to get information about all fields of an object Task
     * @return string information
     */
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getId() + ". " + this.getClass().getSimpleName() + " (" + this.getName() + ", " + this.getStatus() + ", " + this.getDescription());
        stringBuffer.append(", " + this.getNotificationDate() + ", " + this.getPlannedDate() + ")");
        return stringBuffer.toString();
    }
}
