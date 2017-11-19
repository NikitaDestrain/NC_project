package GUI.MainForm;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
    private JButton addTask;
    private JButton editTask;
    private JButton deleteTask;

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
    }
}
