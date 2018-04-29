package server.model;

import server.controller.ParameterParser;
import server.controller.SQLDataAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * The class serves to store the object "task" with server.exceptions.properties
 * <b>name</b>, <b>status</b>, <b>description</b>, <b>notificationDate</b>,  <b>plannedDate</b>, <b>id</b>.
 */

@Entity
@Table(name = "\"Tasks\"", schema = "public", catalog = "cracker")

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Comparable<Task>, Serializable {

    /**
     * unique id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "\"auto_increment\"", allocationSize = 1)
    @Column(name = "\"Task_id\"", nullable = false, unique = true)
    @XmlElement(name = "id")
    private int id;

    /**
     * Task name
     */
    @Column(name = "\"Name\"", nullable = false, unique = true, length = 50)
    @XmlElement(name = "name")
    private String name;

    /**
     * Task status @see TaskStatus
     */

    @Transient
    @XmlElement(name = "status")
    private TaskStatus status;

    @Column(name = "\"Status\"", nullable = false, length = 18)
    private String stringStatus;

    /**
     * Task description
     */
    @Column(name = "\"Description\"", length = 80)
    @XmlElement(name = "description")
    private String description;

    /**
     * Notification date
     */
    @Column(name = "\"Notification_date\"", nullable = false)
    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "notificationDate")
    private Date notificationDate;

    @Transient
    @XmlElement(name = "notification")
    private String notification;

    /**
     * Planned date
     */
    @Column(name = "\"Planned_date\"", nullable = false)
    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "plannedDate")
    private Date plannedDate;

    @Transient
    @XmlElement(name = "planned")
    private String planned;

    @Column(name = "\"Upload_date\"", nullable = false)
    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "uploadDate")
    private Date uploadDate;

    @Transient
    @XmlElement(name = "upload")
    private String upload;

    @Column(name = "\"Change_date\"", nullable = false)
    @XmlJavaTypeAdapter(SQLDataAdapter.class)
    @XmlElement(name = "changeDate")
    private Date changeDate;

    @Transient
    @XmlElement(name = "change")
    private String change;

    @Column(name = "\"Journal_id\"", nullable = false)
    @XmlElement(name = "journalId")
    private int journalId;

    @Transient
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
        this.notification = ParameterParser.parseDate(notificationDate);
        this.planned = ParameterParser.parseDate(plannedDate);
        this.upload = ParameterParser.parseDate(uploadDate);
        this.change = ParameterParser.parseDate(changeDate);
        this.stringStatus = status.toString();
    }

    public Task(String name, TaskStatus status, String description, Date notificationDate,
                Date plannedDate, Date uploadDate, Date changeDate, int journalId) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.notificationDate = notificationDate;
        this.plannedDate = plannedDate;
        this.uploadDate = uploadDate;
        this.changeDate = changeDate;
        this.journalId = journalId;
        this.notification = ParameterParser.parseDate(notificationDate);
        this.planned = ParameterParser.parseDate(plannedDate);
        this.upload = ParameterParser.parseDate(uploadDate);
        this.change = ParameterParser.parseDate(changeDate);
        this.stringStatus = status.toString();
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
        this.stringStatus = status.toString();
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
        this.notification = ParameterParser.parseDate(task.getNotificationDate());
        this.planned = ParameterParser.parseDate(task.getPlannedDate());
        this.upload = ParameterParser.parseDate(task.getUploadDate());
        this.change = ParameterParser.parseDate(task.getChangeDate());
        this.stringStatus = task.getStatus().toString();
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
        this.stringStatus = status.toString();
        this.status = status;
    }

    public String getStringStatus() {
        return stringStatus;
    }

    public void setStringStatus(String stringStatus) {
        setStatus(ParameterParser.parseStatus(stringStatus));
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
        if (!this.notificationDate.equals(notificationDate)) {
            this.notificationDate = notificationDate;
            this.notification = ParameterParser.parseDate(notificationDate);
            isRescheduled = true;
        }
        this.notification = ParameterParser.parseDate(notificationDate);
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
        if (!this.plannedDate.equals(plannedDate)) {
            this.plannedDate = plannedDate;
            this.planned = ParameterParser.parseDate(plannedDate);
            isRescheduled = true;
        }
        this.planned = ParameterParser.parseDate(plannedDate);
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
        this.upload = ParameterParser.parseDate(uploadDate);
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
        this.change = ParameterParser.parseDate(changeDate);
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", stringStatus='" + stringStatus + '\'' +
                ", description='" + description + '\'' +
                ", notificationDate=" + notificationDate +
                ", notification='" + notification + '\'' +
                ", plannedDate=" + plannedDate +
                ", planned='" + planned + '\'' +
                ", uploadDate=" + uploadDate +
                ", upload='" + upload + '\'' +
                ", changeDate=" + changeDate +
                ", change='" + change + '\'' +
                ", journalId=" + journalId +
                ", isRescheduled=" + isRescheduled +
                '}';
    }

    /**
     * Method that allows you to get information about all fields of an object Task
     *
     * @return string information
     */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                journalId == task.journalId &&
                isRescheduled == task.isRescheduled &&
                Objects.equals(name, task.name) &&
                status == task.status &&
                Objects.equals(stringStatus, task.stringStatus) &&
                Objects.equals(description, task.description) &&
                Objects.equals(notificationDate, task.notificationDate) &&
                Objects.equals(notification, task.notification) &&
                Objects.equals(plannedDate, task.plannedDate) &&
                Objects.equals(planned, task.planned) &&
                Objects.equals(uploadDate, task.uploadDate) &&
                Objects.equals(upload, task.upload) &&
                Objects.equals(changeDate, task.changeDate) &&
                Objects.equals(change, task.change);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, status, stringStatus, description, notificationDate, notification, plannedDate, planned, uploadDate, upload, changeDate, change, journalId, isRescheduled);
    }
}
