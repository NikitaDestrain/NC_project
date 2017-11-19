package GUI.MainForm;

import model.TaskStatus;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EventObject;

public class TaskStatusEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox comboBox;

    public TaskStatusEditor() {
        comboBox = new JComboBox(TaskStatus.values());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.setSelectedItem(value);
        comboBox.addActionListener((ActionEvent e) -> fireEditingStopped());
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}
