package GUI.MainForm;

import GUI.Main;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TablePanel extends JPanel {
    private JTable table;
    private JournalTableModel tableModel;
    private JPopupMenu popupMenu;
    private TableListener listener;
    private List<Task> taskList;
    private MainForm form;
    private TaskSender taskSender;

    public TablePanel(MainForm form) {
        this.form = form;
        tableModel = new JournalTableModel();
        table = new JTable(tableModel);
        taskSender = TaskSender.getInstance();
        table.setRowHeight(20);
        popupMenu = new JPopupMenu();
        table.setRowSelectionAllowed(true);

        JMenuItem cancelItem = new JMenuItem("Cancel task");
        popupMenu.add(cancelItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                table.getSelectionModel().setSelectionInterval(row, row);
                Task task = taskList.get(row);

                if (e.getButton() == MouseEvent.BUTTON3) { // правой удаляем
                    popupMenu.show(table, e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1) { // левой выделяем для редактирования
                    if (col == 0) {
                        Boolean b = (Boolean) table.getValueAt(row, col);
                        table.setValueAt(!b, row, col);
                    } else {
                        taskSender.setTask(task);
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
        cancelItem.addActionListener((ActionEvent ev) -> {
            taskList.get(table.getSelectedRow()).setStatus(TaskStatus.Cancelled);
            refresh();
        });

        setLayout(new BorderLayout());
        add(table, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Set tasks to be representing at table
     * @param taskList list with tasks
     */
    public void setData(List<Task> taskList) {
        tableModel.setData(taskList);
        this.taskList = taskList;
    }

    /**
     * Set listener to the <code>TablePanel</code> which listens commands for tasks representing at the table
     * @param listener
     */

    public void setTableListener(TableListener listener) {
        this.listener = listener;
    }

    /**
     * Get <code>JTable</code> of this <code>TablePanel</code>
     * @return
     */

    public JTable getTable() {
        return table;
    }

    /**
     * Refresh values of the table
     */

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
}
