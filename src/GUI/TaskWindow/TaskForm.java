package GUI.TaskWindow;

import GUI.MainForm.ButtonPanel;
import GUI.MainForm.TaskActionListener;

import javax.swing.*;
import java.awt.*;

public class TaskForm extends JFrame {
    private ImageIcon icon = new ImageIcon("icon.png");

    public TaskForm() {
        setIconImage(icon.getImage());
    }

    public void layoutForAdd() {
        setTitle("Add");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    public void layoutForEdit() {
        setTitle("Edit");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }
}
