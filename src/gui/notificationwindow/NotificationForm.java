package gui.notificationwindow;

import controller.Controller;
import gui.mainform.MainForm;
import model.Task;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NotificationForm extends JFrame {
    private Task task;
    private ButtonPanel buttonPanel;
    private LabelPanel labelPanel;
    private ImageIcon icon = new ImageIcon("icon.png");
    private Controller controller = Controller.getInstance();;
    private MainForm mainForm = MainForm.getInstance();

    public NotificationForm() {
        super("Alarm!");
        buttonPanel = new ButtonPanel();
        labelPanel = new LabelPanel();
        setLayout(new BorderLayout());
        add(labelPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        Dimension prefSize = new Dimension(400, 300);
        setPreferredSize(prefSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width - prefSize.width, screenSize.height - prefSize.height - 40, prefSize.width, prefSize.height);
        setIconImage(icon.getImage());
        setResizable(false);
    }

    public void setTask(Task task) {
        this.task = task;
        labelPanel.setName(this.task.getName());
        labelPanel.setDescription(this.task.getDescription());
    }

    private class ButtonPanel extends JPanel {
        private JButton later;
        private JButton finish;
        private JButton cancel;

        public ButtonPanel() {
            later = new JButton("Remind later");
            finish = new JButton("Finish task");
            cancel = new JButton("Cancel task");

            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            setLayout(new FlowLayout());
            add(later);
            add(finish);
            add(cancel);

            Dimension dimension = getPreferredSize();
            dimension.height = 37;
            setPreferredSize(dimension);

            later.addActionListener((ActionEvent e) -> {
                SwingUtilities.invokeLater(() -> new LaterForm(task));
                dispose();
            });

            finish.addActionListener((ActionEvent e) -> {
                controller.finishNotification(task.getId());
                mainForm.updateJournal();
                dispose();
            });

            cancel.addActionListener((ActionEvent e) -> {
                controller.cancelNotification(task.getId());
                mainForm.updateJournal();
                dispose();
            });
        }
    }
}