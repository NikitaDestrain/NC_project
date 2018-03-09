package server.model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

/**
 * The class serves to store the object "task" with server.exceptions.properties
 * <b>name</b>, <b>status</b>, <b>description</b>, <b>notificationDate</b>,  <b>plannedDate</b>, <b>id</b>.
 */
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Comparable<Task>, Serializable {
    /**
     * Task name
     */
    @XmlElement(name = "name")
    private String name;
    /**
     * Task status @see TaskStatus
     */
    @XmlElement(name = "status")
    private TaskStatus status;
    /**
     * Task description
     */
    @XmlElement(name = "description")
    private String description;
    /**
     * Notification date
     */
    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "notificationDate")
    private Date notificationDate;

    @XmlElement(name = "notification")
    private String notification;
    /**
     * Planned date
     */
    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "plannedDate")
    private Date plannedDate;
    @XmlElement(name = "planned")
    private String planned;
    /**
     * unique identificator
     */
    @XmlElement(name = "id")
    private int id;

    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "uploadDate")
    private Date uploadDate;

    @XmlElement(name = "upload")
    private String upload;

    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "changeDate")
    private Date changeDate;

    @XmlElement(name = "change")
    private String change;

    @XmlElement(name = "journalId")
    private int journalId;

    @XmlElement(name = "isRescheduled")
    private boolean isRescheduled;

    public Task() {
    }

    /**
     * Creates a new object with given values
     *
     * @param name
     * @param status
     * @param description
     * @param notificationDate
     * @param plannedDate
     * @param id
     */

    public Task(int id, String name, TaskStatus status, String description, Date notificationDate,
                Date plannedDate, Date uploadDate, Date changeDate, int journalId) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.notificationDate = notificationDate;
        this.plannedDate = plannedDate;
        this.id = id;
        this.uploadDate = uploadDate;
        this.changeDate = changeDate;
        this.journalId = journalId;
        this.notification = parseDate(notificationDate);
        this.planned = parseDate(plannedDate);
        this.upload = parseDate(uploadDate);
        this.change = parseDate(changeDate);
    }

    public Task(String name, TaskStatus status, String description, String notification, String planned, int id, String upload, String change, int journalId) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.notification = notification;
        this.planned = planned;
        this.id = id;
        this.upload = upload;
        this.change = change;
        this.journalId = journalId;
    }

    private String parseDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String days = calendar.get(Calendar.DAY_OF_MONTH) + "";
        days = days.length() == 1 ? "0" + days : days;

        String months = (calendar.get(Calendar.MONTH) + 1) + "";
        months = months.length() == 1 ? "0" + months : months;

        return days + "-" + months + "-" + calendar.get(Calendar.YEAR);
    }

    /**
     * Changes the current task to a new task.
     * values are set:
     * <br><b>name</b>
     * <br><b>status</b>,
     * <br><b>description</b>,
     * <br><b>notificationDate</b>,
     * <br><b>plannedDate</b>.
     *
     * @param task new task
     */
    public void setTask(Task task) {
        this.name = task.getName();
        this.status = task.getStatus();
        this.description = task.getDescription();
        if (this.notificationDate != task.getNotificationDate()) {
            this.notificationDate = task.getNotificationDate();
            isRescheduled = true;
        }
        if (this.plannedDate != task.plannedDate) {
            this.plannedDate = task.getPlannedDate();
            isRescheduled = true;
        }
        this.changeDate = task.getChangeDate();
        this.uploadDate = task.getUploadDate();
        this.notification = parseDate(task.getNotificationDate());
        this.planned = parseDate(task.getPlannedDate());
        this.upload = parseDate(task.getUploadDate());
        this.change = parseDate(task.getChangeDate());
    }

    /**
     * Return field value   <b>name<b/>
     *
     * @return task name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the task field value <b>name<b/>
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the current  value of the task field <b>status<b/>
     *
     * @return {@linkplain TaskStatus } status
     * @see TaskStatus
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Changes the current <b>status</b>,  of the task
     *
     * @param status new status from the class {@linkplain TaskStatus}
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Returns the field value <b>description<b/>
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifies the current task field value  <b>description<b/>
     *
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the field value <b>notificationDate<b/>
     *
     * @return task notification date
     */
    public Date getNotificationDate() {
        return notificationDate;
    }

    /**
     * Set the value of the field <b>notificationDate<b/>
     *
     * @param notificationDate the new task notification time
     */
    public void setNotificationDate(Date notificationDate) {
        if (this.notificationDate != notificationDate) {
            this.notificationDate = notificationDate;
            this.notification = parseDate(notificationDate);
            isRescheduled = true;
        }
    }

    /**
     * Returns the field value <b>plannedDate<b/>
     *
     * @return planned task time
     */
    public Date getPlannedDate() {
        return plannedDate;
    }

    /**
     * Set the value of the field <b>plannedDate<b/>
     *
     * @param plannedDate new planned task time
     */
    public void setPlannedDate(Date plannedDate) {
        if (this.plannedDate != plannedDate) {
            this.plannedDate = plannedDate;
            this.planned = parseDate(plannedDate);
            isRescheduled = true;
        }
    }

    /**
     * Returns the field value <b>id</b>
     *
     * @return unique identificator
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
        this.upload = parseDate(uploadDate);
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
        this.change = parseDate(changeDate);
    }

    public String getNotification() {
        return notification;
    }

    public String getPlanned() {
        return planned;
    }

    public String getUpload() {
        return upload;
    }

    public String getChange() {
        return change;
    }

    public int getJournalId() {
        return journalId;
    }

    public void setJournalId(int journalId) {
        this.journalId = journalId;
    }

    public boolean isRescheduled() {
        return isRescheduled;
    }

    //вернет -1 если у задачи переданной в кач-ве аргумента дата идет позже

    /**
     * Method allows you to compare two tasks by the time of notification
     *
     * @param task object of the task with which the object that called the method is compared
     * @return <b>-1</b> if notification the transferred task object > than notificaton time of this task notification time.
     * Else <b>1</b>
     */
    @Override
    public int compareTo(Task task) {
        if (task.getNotificationDate() != null) {
            if (notificationDate.compareTo(task.getNotificationDate()) < 0)
                return -1;
            if (notificationDate.compareTo(task.getNotificationDate()) > 0)
                return 1;
        }
        return 0;
    }

    /**
     * Method that allows you to get information about all fields of an object Task
     *
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
