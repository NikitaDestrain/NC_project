package client.gui.mainform;

import client.commandprocessor.ClientCommandSender;
import client.model.Task;
import client.model.TaskStatus;
import client.network.ClientNetworkFacade;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JournalTableModel extends AbstractTableModel {
    private List<Task> tasks;
    private String[] columnNames = {"*", "Status", "Name", "Description", "Planned date", "Planned time", "Notification date",
            "Notification time"};
    private Object[][] data;
    private ClientCommandSender commandSender = ClientCommandSender.getInstance();
    private ClientNetworkFacade clientFacade = ClientNetworkFacade.getInstance();

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
        } else return -1;
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
                return planned.get(Calendar.DAY_OF_MONTH) + "." + (planned.get(Calendar.MONTH) + 1) + "." + planned.get(Calendar.YEAR);// + " " + task.getPlannedDate().getTime();
            case 5:
                minutes = planned.get(Calendar.MINUTE) + "";
                minutes = minutes.length() == 1 ? "0" + minutes : minutes;
                return planned.get(Calendar.HOUR_OF_DAY) + ":" + minutes;// + " " + task.getNotificationDate().getTime();
            case 6:
                return notif.get(Calendar.DAY_OF_MONTH) + "." + (notif.get(Calendar.MONTH) + 1) + "." + notif.get(Calendar.YEAR);
            case 7:
                minutes = notif.get(Calendar.MINUTE) + "";
                minutes = minutes.length() == 1 ? "0" + minutes : minutes;
                return notif.get(Calendar.HOUR_OF_DAY) + ":" + minutes;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Task task = tasks.get(rowIndex);
        System.out.println(task);
        switch (columnIndex) {
            case 0:
                data[rowIndex][0] = aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
                return;
            case 2:
                String value = (String) aValue;
                if (value.equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Task name should not be empty!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    task.setName((String) aValue);
                    commandSender.sendEditCommand(task, clientFacade.getDataOutputStream());
                    return;
                }
            case 3:
                task.setDescription((String) aValue);
                commandSender.sendEditCommand(task, clientFacade.getDataOutputStream());
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
