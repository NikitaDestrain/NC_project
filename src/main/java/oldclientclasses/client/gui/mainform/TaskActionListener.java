package oldclientclasses.client.gui.mainform;

import server.model.Task;

public interface TaskActionListener {
    int ADD_TASK = 0;
    int EDIT_TASK = 1;
    int DELETE_TASK = 2;

    void setTask(int action, Task task);

    void setAction(int action);
}
