package GUI.MainForm;

import controller.Notifier;
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

    public TablePanel() {
        tableModel = new JournalTableModel();
        table = new JTable(tableModel);
        table.setRowHeight(20);
        popupMenu = new JPopupMenu();

        table.setDefaultRenderer(TaskStatus.class, new TaskStatusRenderer());
        table.setDefaultEditor(TaskStatus.class, new TaskStatusEditor());

        /**
         * УТОЧНИТЬ
         */
        JMenuItem removeItem = new JMenuItem("Delete task");
        popupMenu.add(removeItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                table.getSelectionModel().setSelectionInterval(row, row);

                if (e.getButton() == MouseEvent.BUTTON3) { // правой удаляем
                    popupMenu.show(table, e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1) { // левой выделяем для редактирования
                    if (listener != null) {
                        listener.rowDeleted(row);
                    }
                    Task task = taskList.get(row); // параметры этой задачи передаются в окно редактирования
                    System.out.println(task.toString());
                }
            }
        });

        removeItem.addActionListener((ActionEvent e) -> {
            new Notifier().setTask(); // тестовый
        });

        setLayout(new BorderLayout());
        add(table, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setData(List<Task> taskList) {
        tableModel.setData(taskList);
        this.taskList = taskList;
    }

    public void setTableListener(TableListener listener) {
        this.listener = listener;
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
}
