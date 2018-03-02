package server.gui.mainform;

import server.gui.taskwindow.TaskWindow;
import server.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonPanel extends JPanel implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    /*
    private JButton addTask;
    private JButton editTask;
    private JButton deleteTask;
    private TaskActionListener listener;
    private JTable jtable;
    private TableListener tableListener;
    private Task task;
    private MainForm owner;
    private TaskSender taskSender;

    public ButtonPanel(MainForm owner) {
        this.owner = owner;
        addTask = new JButton("Add task");
        editTask = new JButton("Edit task");
        deleteTask = new JButton("Delete task");
        taskSender = TaskSender.getInstance();

        setLayout(new FlowLayout());
        add(addTask);
        add(editTask);
        add(deleteTask);

        Dimension dimension = getPreferredSize();
        dimension.height = 37;
        setPreferredSize(dimension);

        addTask.addActionListener(this);

        editTask.addActionListener(this);

        deleteTask.addActionListener(this);
    }

    public void setListener(TaskActionListener listener) {
        this.listener = listener;
    }

    public void setTableListener(TableListener tableListener) {
        this.tableListener = tableListener;
    }

    public void setJtable(JTable jtable) {
        this.jtable = jtable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == addTask) {
            new TaskWindow(owner);
        } else if (clicked == editTask) {
            task = taskSender.getTask();
            taskSender.clearTask();
            if (task != null)
                listenerAction(TaskActionListener.EDIT_TASK, task);
        } else if (clicked == deleteTask) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < jtable.getRowCount(); i++) {
                Boolean b = (Boolean) jtable.getValueAt(i, 0);
                if (b) {
                    list.add(i);
                }
            }
            Object[] objs = list.toArray();
            Integer[] rows = new Integer[objs.length];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = Integer.parseInt(objs[i].toString());
            }
            tableListener.rowDeleted(rows);
            listenerAction(TaskActionListener.DELETE_TASK);
        }
    }

    private void listenerAction(int action, Task task) {
        if (listener != null) {
            listener.setTask(action, task);
        }
    }

    private void listenerAction(int action) {
        if (listener != null) {
            listener.setAction(action);
        }
    }
*/
}
