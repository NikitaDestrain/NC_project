package GUI.MainForm;

import GUI.TaskWindow.TaskForm;
import controller.Notifier;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class ButtonPanel extends JPanel implements ActionListener {
    private JButton addTask;
    private JButton editTask;
    private JButton deleteTask;
    private TaskActionListener listener;
    private TablePanel tablePanel;

    public ButtonPanel() {
        addTask = new JButton("Add task");
        editTask = new JButton("Edit task");
        deleteTask = new JButton("Delete task");

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
        //            tableListener.rowDeleted(row);
//            tableModel.fireTableRowsDeleted(row,row);
    }

    public void setListener(TaskActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked == addTask) {
            listenerAction(TaskActionListener.ADD_TASK);
        }
        else if (clicked == editTask) {
            listenerAction(TaskActionListener.EDIT_TASK);
        }
        else if (clicked == deleteTask) {
            listenerAction(TaskActionListener.DELETE_TASK);
        }
    }

    private void listenerAction(int action) {
        if (listener != null) {
            listener.setAction(action);
        }
    }

    public void setTablePanel(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }
}
