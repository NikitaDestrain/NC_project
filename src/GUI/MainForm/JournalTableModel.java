package GUI.MainForm;

import model.Task;
import model.TaskStatus;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class JournalTableModel extends AbstractTableModel {
    private List<Task> tasks;
    private String[] columnNames = {"*", "ID", "Status", "Name", "Description", "Planned date", "Notification date"};
    private Object[][] data;

    // TODO CHECKBOXES, КНОПКИ ЛАЙФСАЙКЛА - отменить, отложить

    public JournalTableModel() {
    }

    public void setData(List<Task> tasks) {
        this.tasks = tasks;
        data = new Object[tasks.size()][7];
        for (int j = 0; j < tasks.size(); j++) {
            data[j][0] = false;
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        if (tasks != null) {
            return tasks.size();
        }
        else return -1;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3 || columnIndex == 4 || columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return data[rowIndex][0];
            case 1:
                return task.getId();
            case 2:
                return task.getStatus();
            case 3:
                return task.getName();
            case 4:
                return task.getDescription();
            case 5:
                return task.getPlannedDate();
            case 6:
                return task.getNotificationDate();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0:
                data[rowIndex][0] = aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
                return;
            case 3:
                task.setName((String) aValue);
                return;
            case 4:
                task.setDescription((String) aValue);
                return;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return Integer.class;
            case 2:
                return TaskStatus.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return Date.class;
            case 6:
                return Date.class;
            default:
                return null;
        }
    }
}
