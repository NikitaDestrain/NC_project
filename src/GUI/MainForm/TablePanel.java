package GUI.MainForm;

import controller.Notifier;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
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
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    switch (e.getColumn()) {
                        case 0 :
                            break;
                        case 3:
                            Task task = taskList.get(row);
                            task.setName((String)tableModel.getValueAt(row, col));
                            refresh();
                            break;
                    }
                }
            }
        });
        table = new JTable(tableModel);
        table.setRowHeight(20);
        popupMenu = new JPopupMenu();

        DefaultCellEditor cellEditor = new DefaultCellEditor(new JCheckBox());
        cellEditor.setClickCountToStart(1);
        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                //tableModel.setValueAt();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });

       table.setDefaultEditor(Boolean.class, new CheckBoxEditor());
        table.getColumnModel().getColumn(0).setCellEditor(cellEditor);
        //table.setDefaultRenderer(Boolean.class, new CheckBoxRenderer());

        JMenuItem deferItem = new JMenuItem("Defer task");
        JMenuItem cancelItem = new JMenuItem("Cancel task");
        popupMenu.add(deferItem);
        popupMenu.add(cancelItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                table.getSelectionModel().setSelectionInterval(row, row);
                Task task = taskList.get(row);

                if (e.getButton() == MouseEvent.BUTTON3) { // правой удаляем
                    popupMenu.show(table, e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1) { // левой выделяем для редактирования
                    if (listener != null) {
                        listener.rowDeleted(row);
                    }
                    //Task task = taskList.get(row); // параметры этой задачи передаются в окно редактирования
                    System.out.println(task.toString());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        deferItem.addActionListener((ActionEvent ev) -> {
            taskList.get(table.getSelectedRow()).setStatus(TaskStatus.Waiting);
            refresh();
        });
        cancelItem.addActionListener((ActionEvent ev) -> {
            taskList.get(table.getSelectedRow()).setStatus(TaskStatus.Cancelled);
            refresh();
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
