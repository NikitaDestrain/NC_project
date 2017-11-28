package gui.notificationwindow;

import model.Task;

import javax.swing.*;
import java.awt.*;

public class NotificationForm extends JFrame {
    private Task task;
    private ButtonPanel buttonPanel;
    private LabelPanel labelPanel;
    private ImageIcon icon = new ImageIcon("icon.png");

    public NotificationForm() {
        super("Warning!");
        buttonPanel = new ButtonPanel();
        labelPanel = new LabelPanel();

        setLayout(new BorderLayout());
        add(labelPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        Dimension prefSize = new Dimension(400, 300);
        setPreferredSize(prefSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width - prefSize.width, screenSize.height - prefSize.height - 40,
                prefSize.width, prefSize.height);
        setIconImage(icon.getImage());
        setResizable(false);
        setVisible(false);
    }

    public void setTask(Task task) {
        this.task = task;
        labelPanel.setName(this.task.getName());
        labelPanel.setDescription(this.task.getDescription());
    }
}
