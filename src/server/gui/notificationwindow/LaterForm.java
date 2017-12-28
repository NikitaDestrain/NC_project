package server.gui.notificationwindow;

import server.commandproccessor.ServerCommandSender;
import server.controller.Controller;
import server.exceptions.IllegalPropertyException;
import server.model.Task;
import server.network.ServerNetworkFacade;
import server.properties.ParserProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.Date;

public class LaterForm extends JFrame {
    private JLabel message;
    private ButtonLaterPanel buttonLaterPanel;
    private WriteLaterPanel writeLaterPanel;
    private ImageIcon icon;
    private ServerCommandSender commandSender = ServerCommandSender.getInstance();
    private ServerNetworkFacade facade = ServerNetworkFacade.getInstance();

    public LaterForm(Task task){
        setLayout(new GridLayout(3,1));
        try {
            icon = new ImageIcon(ParserProperties.getInstance().getProperties("MAIN_FORM_ICON"));//todo vlla вынести все константы в специаьный класс
        } catch (IllegalPropertyException e) {
            JOptionPane.showMessageDialog(null, "Illegal value of property",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        message = new JLabel("Choose the time for reschedule");
        message.setFont(new Font("Verdana", Font.BOLD, 18));
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        add(message);
        buttonLaterPanel = new ButtonLaterPanel(task);
        writeLaterPanel = new WriteLaterPanel(task);
        add(writeLaterPanel);
        add(buttonLaterPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        Dimension prefSize = new Dimension(400, 300);
        setPreferredSize(prefSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width - prefSize.width, screenSize.height - prefSize.height - 70, prefSize.width, prefSize.height);
        setIconImage(icon.getImage());
        setVisible(true);
    }

    private class WriteLaterPanel extends JPanel{
        private JLabel hoursLabel;
        private JLabel minutesLabel;
        private JSpinner hoursSpinner;
        private JSpinner minutesSpinner;
        private JButton okButton;
        private Controller controller = Controller.getInstance();

        public WriteLaterPanel(Task task){
            hoursLabel = new JLabel("hours:");
            minutesLabel = new JLabel("minutes:");
            hoursSpinner = new JSpinner();
            minutesSpinner = new JSpinner();
            hoursSpinner.setModel(new SpinnerNumberModel(0,0,23,1));
            minutesSpinner.setModel(new SpinnerNumberModel(1,1,59,1));

            okButton = new JButton("OK");
            okButton.addActionListener((ActionEvent e) -> {
                if(controller.getJournal().getTask(task.getId()) != null) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + (int) this.hoursSpinner.getValue() * 60 * 60000
                            + (int) this.minutesSpinner.getValue() * 60000));
                    controller.updateNotification(task);
                }
                else
                    JOptionPane.showMessageDialog(null, "This task has been deleted! It is not able to be rescheduled!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            });

            add(hoursLabel);
            add(hoursSpinner);
            add(minutesLabel);
            add(minutesSpinner);
            add(okButton);
        }

    }

    private class ButtonLaterPanel extends JPanel {
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
                if(controller.getJournal().getTask(task.getId()) != null) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 5 * 60000));
                    controller.updateNotification(task);
                }
                else
                    JOptionPane.showMessageDialog(null, "This task has been deleted! It is not able to be rescheduled!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            });

            later15.addActionListener((ActionEvent e) -> {
                if(controller.getJournal().getTask(task.getId()) != null) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 15 * 60000));
                    controller.updateNotification(task);
                }
                else
                    JOptionPane.showMessageDialog(null, "This task has been deleted! It is not able to be rescheduled!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            });

            later30.addActionListener((ActionEvent e) -> {
                if(controller.getJournal().getTask(task.getId()) != null) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 30 * 60000));
                    controller.updateNotification(task);
                }
                else
                    JOptionPane.showMessageDialog(null, "This task has been deleted! It is not able to be rescheduled!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            });

            later60.addActionListener((ActionEvent e) -> {
                if(controller.getJournal().getTask(task.getId()) != null) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 60 * 60000));
                    controller.updateNotification(task);
                }
                else
                    JOptionPane.showMessageDialog(null, "This task has been deleted! It is not able to be rescheduled!",
                            "Error", JOptionPane.ERROR_MESSAGE);
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
