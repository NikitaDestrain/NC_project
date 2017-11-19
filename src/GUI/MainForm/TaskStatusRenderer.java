package GUI.MainForm;

import model.TaskStatus;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TaskStatusRenderer implements TableCellRenderer {
    JComboBox comboBox;

    public TaskStatusRenderer() {
        comboBox = new JComboBox(TaskStatus.values());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        comboBox.setSelectedItem(value);
        return comboBox;
    }
}
