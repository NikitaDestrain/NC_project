package GUI.MainForm;

import GUI.TaskWindow.TaskForm;
import controller.Notifier;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

public class ButtonPanel extends JPanel implements ActionListener {
    private JButton addTask;
    private JButton editTask;
    private JButton deleteTask;
    private TaskActionListener listener;
    private JTable table;
    private TableListener tableListener;

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
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                Boolean b = (Boolean)table.getValueAt(i, 0);
                if (b) {
                    list.add(i);
                }
            }
            Object[] objs = list.toArray();
            Integer[] rows = new Integer[objs.length];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = Integer.parseInt(objs[i].toString());
            }
            //DefaultTableModel model = (DefaultTableModel) table.getModel();
//            int [] rows = table.getSelectedRows();
//            Integer [] rowsInt = new Integer[rows.length];
//            for (int i = 0; i < rows.length; i++) {
//                rowsInt[i] = rows[i];
//            }
            tableListener.rowDeleted(rows);
            listenerAction(TaskActionListener.DELETE_TASK);
        }
    }

    private void listenerAction(int action) {
        if (listener != null) {
            listener.setAction(action);
        }
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public void setTableListener(TableListener tableListener) {
        this.tableListener = tableListener;
    }
}
