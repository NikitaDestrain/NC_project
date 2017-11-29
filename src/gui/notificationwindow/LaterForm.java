package gui.notificationwindow;

import controller.Controller;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class LaterForm extends JFrame {
    private JLabel message;
    private ButtonLaterPanel buttonLaterPanel;
    private ImageIcon icon = new ImageIcon("icon.png");

    public LaterForm(Task task){
        message = new JLabel("Choose the time for reschedule");
        message.setFont(new Font("Verdana", Font.BOLD, 15));
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        add(message);
        buttonLaterPanel = new ButtonLaterPanel(task);
        add(buttonLaterPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        Dimension prefSize = new Dimension(400, 300);
        setPreferredSize(prefSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width - prefSize.width, screenSize.height - prefSize.height - 40, prefSize.width, prefSize.height);
        setIconImage(icon.getImage());
        setVisible(true);
    }

    public class ButtonLaterPanel extends JPanel {
        private JButton later5;
        private JButton later15;
        private JButton later30;
        private JButton later60;
        private Controller controller = Controller.getInstance();

        public ButtonLaterPanel(Task task) {
            later5 = new JButton("5 minute");
            later15 = new JButton("15 minute");
            later30 = new JButton("30 minute");
            later60 = new JButton("1 hour");

            later5.addActionListener((ActionEvent e) -> {
                task.setNotificationDate(new Date(task.getNotificationDate().getTime() + 5 * 60000));
                controller.updateNotification(task.getId());
                dispose();
            });

            later15.addActionListener((ActionEvent e) -> {
                task.setNotificationDate(new Date(task.getNotificationDate().getTime() + 15 * 60000));
                controller.updateNotification(task.getId());
                dispose();
            });

            later30.addActionListener((ActionEvent e) -> {
                task.setNotificationDate(new Date(task.getNotificationDate().getTime() + 30 * 60000));
                controller.updateNotification(task.getId());
                dispose();
            });

            later60.addActionListener((ActionEvent e) -> {
                task.setNotificationDate(new Date(task.getNotificationDate().getTime() + 60 * 60000));
                controller.updateNotification(task.getId());
                dispose();
            });

            setLayout(new FlowLayout());
            add(later5);
            add(later15);
            add(later30);
            add(later60);


            Dimension dimension = getPreferredSize();
            dimension.height = 37;
            setPreferredSize(dimension);
        }
    }
}
