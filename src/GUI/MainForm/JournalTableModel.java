package GUI.MainForm;

import model.Task;
import model.TaskStatus;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class JournalTableModel extends AbstractTableModel {
    private List<Task> tasks;
    private String[] columnNames = {"ID", "Status", "Name", "Description", "Planned date", "Notification date"};

    public JournalTableModel() {}

    public void setData(List<Task> tasks) {
        this.tasks = tasks;
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
        return tasks == null ? -1 : tasks.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0 :
                return task.getId();
            case 1 :
                return task.getStatus();
            case 2 :
                return task.getName();
            case 3 :
                return task.getDescription();
            case 4 :
                return task.getPlannedDate();
            case 5 :
                return task.getNotificationDate();
            default :
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return Integer.class;
            case 1 :
                return TaskStatus.class;
            case 2 :
                return String.class;
            case 3 :
                return String.class;
            case 4 :
                return Date.class;
            case 5 :
                return Date.class;
            default :
                return null;
        }
    }
}
