package gui.mainform;

import model.Task;
import model.TaskStatus;

import javax.swing.table.AbstractTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JournalTableModel extends AbstractTableModel {
    private List<Task> tasks;
    private String[] columnNames = {"*", "Status", "Name", "Description", "Planned date", "Planned time", "Notification date",
    "Notification time"};
    private Object[][] data;

    // TODO CHECKBOXES, КНОПКИ ЛАЙФСАЙКЛА - отменить, отложить

    public JournalTableModel() {
    }

    public void setData(List<Task> tasks) {
        this.tasks = tasks;
        data = new Object[tasks.size()][8];
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
        return columnIndex == 2 || columnIndex == 3 || columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        Calendar planned = Calendar.getInstance();
        Calendar notif = Calendar.getInstance();
        planned.setTime(task.getPlannedDate());
        notif.setTime(task.getNotificationDate());
        String minutes = "";
        switch (columnIndex) {
            case 0:
                return data[rowIndex][0];
            case 1:
                return task.getStatus();
            case 2:
                return task.getName();
            case 3:
                return task.getDescription();
            case 4:
                return planned.getTime();// + " " + task.getPlannedDate().getTime();
            case 5:
                minutes = planned.get(Calendar.MINUTE) == 0 ? planned.get(Calendar.MINUTE) + "0" : planned.get(Calendar.MINUTE) + "";
                return planned.get(Calendar.HOUR_OF_DAY) + ":" + minutes;// + " " + task.getNotificationDate().getTime();
            case 6:
                return notif.getTime();
            case 7:
                minutes = notif.get(Calendar.MINUTE) == 0 ? notif.get(Calendar.MINUTE) + "0" : notif.get(Calendar.MINUTE) + "";
                return notif.get(Calendar.HOUR_OF_DAY) + ":" + minutes;
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
            case 2:
                task.setName((String) aValue);
                return;
            case 3:
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
                return TaskStatus.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Date.class;
            case 5:
                return String.class;
            case 6:
                return Date.class;
            case 7:
                return String.class;
            default:
                return null;
        }
    }
}
