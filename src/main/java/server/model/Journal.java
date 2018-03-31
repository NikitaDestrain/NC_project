package server.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

//todo доделать связь onetomany
@Entity
@Table(name = "\"Journal\"", schema = "public", catalog = "postgres")

@XmlType(propOrder = {"id", "name", "description", "userId", "tasks"}, name = "journal")
@XmlRootElement(name = "journal")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Task.class, TaskStatus.class})
public class Journal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "\"auto_increment\"", allocationSize = 1)
    @Column(name = "\"Journal_id\"", nullable = false, unique = true)
    @XmlElement(name = "id")
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @XmlElement(name = "tasks")
    private Map<Integer, Task> tasks;

    @Column(name = "\"Name\"", nullable = false, length = 18, unique = true)
    @XmlElement(name = "name")
    private String name;

    @Column(name = "\"Description\"", length = 80)
    @XmlElement(name = "description")
    private String description;

    @Column(name = "\"User_id\"", nullable = false)
    @XmlElement(name = "userId")
    private int userId;

    /**
     * Creates new Journal object and instantiates new {@code HashMap} of tasks
     */
    public Journal() {
        tasks = new LinkedHashMap<>();
    }

    public Journal(int id, String name, String description, int userId) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.userId = userId;
        tasks = new LinkedHashMap<>();
    }

    public Journal(String name, String description, int userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        tasks = new LinkedHashMap<>();
    }

    public Journal(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = 0;
        this.userId = 0;
        tasks = new LinkedHashMap<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Puts the task by its id to the map
     *
     * @param task
     */

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * Removes the task from map by its id
     *
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
     *
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

    public int getMaxId() {
        int res = 0;
        for (Integer key : tasks.keySet())
            if (res < key)
                res = key;
        return res;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.getClass().getSimpleName() + " " + this.name + " " + this.description +
                " " + this.userId + " (" + this.tasks + ")");
        return stringBuffer.toString();
    }
}
