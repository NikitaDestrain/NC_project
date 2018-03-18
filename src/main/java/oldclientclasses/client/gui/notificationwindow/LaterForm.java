package oldclientclasses.client.gui.notificationwindow;

import javax.swing.*;

public class LaterForm extends JFrame {
    /*
    private JLabel message;
    private ButtonLaterPanel buttonLaterPanel;
    private WriteLaterPanel writeLaterPanel;
    private ImageIcon icon;
    private ClientNetworkFacade clientFacade = ClientNetworkFacade.getInstance();
    private ClientCommandSender commandSender = ClientCommandSender.getInstance();
    private Task task;
    private MessageBox messageBox = MessageBox.getInstance();

    public LaterForm(Task t){
        task = t;
        setLayout(new GridLayout(3,1));
        try {
            icon = new ImageIcon(ParserProperties.getInstance().getProperties(ConstantsClass.MAIN_FORM_ICON));
        } catch (IllegalPropertyException e) {
            JOptionPane.showMessageDialog(null, "Illegal value of property",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "The configuration file is corrupt or missing!",
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
        private Task task;

        public WriteLaterPanel(Task t){
            task = t;
            hoursLabel = new JLabel("hours:");
            minutesLabel = new JLabel("minutes:");
            hoursSpinner = new JSpinner();
            minutesSpinner = new JSpinner();
            hoursSpinner.setModel(new SpinnerNumberModel(0,0,23,1));
            minutesSpinner.setModel(new SpinnerNumberModel(1,1,59,1));

            okButton = new JButton("OK");
            okButton.addActionListener((ActionEvent e) -> {
                if(checkAction()) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + (int) this.hoursSpinner.getValue() * 60 * 60000
                            + (int) this.minutesSpinner.getValue() * 60000));
                    try {
                        commandSender.sendLaterCommand(task, clientFacade.getDataOutputStream());
                    } catch (UnsuccessfulCommandActionException e1) {
                        messageBox.showAskForRestartMessage();
                    }
                }
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
        private Task task;

        public ButtonLaterPanel(Task t) {
            later5 = new JButton("5 minute");
            later15 = new JButton("15 minute");
            later30 = new JButton("30 minute");
            later60 = new JButton("1 hour");
            task = t;

            later5.addActionListener((ActionEvent e) -> {
                if(checkAction()) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 5 * 60000));
                    try {
                        commandSender.sendLaterCommand(task, clientFacade.getDataOutputStream());
                    } catch (UnsuccessfulCommandActionException e1) {
                        messageBox.showAskForRestartMessage();
                    }
                }
                dispose();
            });

            later15.addActionListener((ActionEvent e) -> {
                if(checkAction()) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 15 * 60000));
                    try {
                        commandSender.sendLaterCommand(task, clientFacade.getDataOutputStream());
                    } catch (UnsuccessfulCommandActionException e1) {
                        messageBox.showAskForRestartMessage();
                    }
                }
                dispose();
            });

            later30.addActionListener((ActionEvent e) -> {
                if(checkAction()) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 30 * 60000));
                    try {
                        commandSender.sendLaterCommand(task, clientFacade.getDataOutputStream());
                    } catch (UnsuccessfulCommandActionException e1) {
                        messageBox.showAskForRestartMessage();
                    }
                }
                dispose();
            });

            later60.addActionListener((ActionEvent e) -> {
                if(checkAction()) {
                    task.setNotificationDate(new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.MILLISECOND)
                            - Calendar.getInstance().get(Calendar.SECOND) * 1000 + 60 * 60000));
                    try {
                        commandSender.sendLaterCommand(task, clientFacade.getDataOutputStream());
                    } catch (UnsuccessfulCommandActionException e1) {
                        messageBox.showAskForRestartMessage();
                    }
                }
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

    private boolean checkAction() {
        if(MainForm.getInstance().getJournal().getTask(task.getId()) == null) {
            JOptionPane.showMessageDialog(null, "No task to perform this action",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(MainForm.getInstance().getJournal().getTask(task.getId()).getStatus() == TaskStatus.Completed) {
            JOptionPane.showMessageDialog(null, "This task has been already completed by another user!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(MainForm.getInstance().getJournal().getTask(task.getId()).getStatus() == TaskStatus.Cancelled) {
            JOptionPane.showMessageDialog(null, "This task has been already cancelled by another user!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }*/
}
