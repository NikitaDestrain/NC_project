package GUI.MainForm;

import controller.Notifier;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

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

        addTask.addActionListener((ActionEvent e) -> {
            new Notifier().setTask(); // тестовый
        });

        editTask.addActionListener((ActionEvent e) -> {

        });

        deleteTask.addActionListener((ActionEvent e) -> {

        }) ;
    }
}
