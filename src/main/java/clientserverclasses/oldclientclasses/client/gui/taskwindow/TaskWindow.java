package clientserverclasses.oldclientclasses.client.gui.taskwindow;

import javax.swing.*;

public class TaskWindow extends JFrame {
/*
    private MainForm owner;
    private Task loadTask;
    private JLabel jLabel_remind_for;
    private JComboBox jComboBox_remindBefore;
    private JLabel jLabel1_before_the;
    private JLabel JTabe_nameTask;
    private JButton jButton_cancel;
    private JButton jButton_create_or_set;
    private JButton jButton_CancelTask;
    private JButton jButton_TaskCompleted;
    private com.toedter.calendar.JDateChooser jDateChooser_PlannedDate;
    private com.toedter.calendar.JDateChooser jDateChooser_notificationDate;
    private JLabel jLabel_Hours;
    private JLabel jLabel_dics;
    private JLabel jLabel_minutes;
    private JLabel jLabel_notifMinutes;
    private JLabel jLabel_notification_date;
    private JLabel jLabel_planned_date;
    private JLabel jLabel_status;
    private JLabel jLabel_statusInfo;
    private JLabel jLable_jangeStatus;
    private JLabel jLable_notifhour;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JSpinner jSpinner_plannedHour;
    private JSpinner jSpinner_plannedMin;
    private JSpinner jSpinner_notifHour;
    private JSpinner jSpinner_notifMinutes;
    private JTextArea jTextArea_descriprion;
    private JTextField jTextField_name;
    private JSpinner jSpinner_remindBefore_hour;
    private JSpinner jSpinner_remindMinutes;
    private JLabel jLabel_remindHours;
    private JLabel jLabel_RemindBefore_minutes;
    private Date plannedDate;//запланированное время
    private Date notificationDate; //время уведомления
    private ClientNetworkFacade clientFacade = ClientNetworkFacade.getInstance();
    private ClientCommandSender commandSender = ClientCommandSender.getInstance();
    private MessageBox messageBox = MessageBox.getInstance();

    //Перегруженные конструкторы для создания и просмотра/редактирования
    public TaskWindow(MainForm owner) {

        super("Create new task");
        this.initComponentsCreateTask();
        this.owner = owner;
        this.jButton_create_or_set.setText("Create");
        this.jButton_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        //создаем новую таску
        this.jButton_create_or_set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Task newTask = createTask();

                if (newTask != null) {
                    mainFormAddTask(newTask);
                    dispose();
                }

            }
        });
        setVisible(true);
    }

    public TaskWindow(MainForm owner, Task task) {

        super("Task Info");
        this.owner = owner;
        this.loadTask = task;
        setLocationRelativeTo(null); //установка по центру экрана
        this.initComponentsEditTask();
        this.paintTask(task);
        //закрываем по cancel
        this.jButton_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
        //сохраняем изменения в новую таску
        this.jButton_create_or_set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (saveTask()) {

                    mainFormEditTask(loadTask);
                    dispose();

                }

            }
        });
        this.jButton_CancelTask.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    commandSender.sendCancelCommand(loadTask, clientFacade.getDataOutputStream());
                } catch (UnsuccessfulCommandActionException e1) {
                    messageBox.showAskForRestartMessage();
                }
                dispose();
            }
        });

        this.jButton_TaskCompleted.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    commandSender.sendFinishCommand(task, clientFacade.getDataOutputStream());
                } catch (UnsuccessfulCommandActionException e1) {
                    messageBox.showAskForRestartMessage();
                }
                dispose();
            }
        });
        setVisible(true);
    }


    private void initComponentsCreateTask() {

        setSize(new Dimension(417, 301));
        setResizable(false);

        setLocationRelativeTo(null); //установка по центру экрана

        JTabe_nameTask = new JLabel("Task name:");
        jTextField_name = new JTextField();

        jLabel_planned_date = new JLabel("Planned date:");
        jDateChooser_PlannedDate = new com.toedter.calendar.JDateChooser();
        jDateChooser_PlannedDate.setLocale(Locale.ENGLISH);
        jDateChooser_PlannedDate.setMaxSelectableDate(new Date(253370840399000L));//max data
        jDateChooser_PlannedDate.setMinSelectableDate(new Date(System.currentTimeMillis()));

        jComboBox_remindBefore = new JComboBox<>();
        jComboBox_remindBefore.setModel(new DefaultComboBoxModel<>(new String[]{"0 minutes", "1 minute", "5 minutes", "10 minutes", "15 minutes", "20 minutes", "25 minutes", "30 minutes", "35 minutes", "40 minutes", "45 minutes", "50 minutes", "55 minutes", "1 hour", "2 hours", "3 hours", "6 hours", "9 hours", "12 hours", "24 hours"}));

        jLabel_remind_for = new JLabel("Remind for");
        jLabel1_before_the = new JLabel("before the start.");

        jLabel_Hours = new JLabel("Hours");
        jLabel_minutes = new JLabel("Minutes");

        jLabel_dics = new JLabel("Description:");

        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();

        jTextArea_descriprion = new JTextArea();
        jTextArea_descriprion.setColumns(30);

        jTextArea_descriprion.setRows(5);

        jButton_create_or_set = new JButton();
        jButton_cancel = new JButton();
        jButton_cancel.setText("Cancel");

        jSpinner_plannedMin = new JSpinner();
        jSpinner_plannedHour = new JSpinner();
        jSpinner_remindBefore_hour = new JSpinner();
        jSpinner_remindMinutes = new JSpinner();
        jSpinner_plannedHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_plannedMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        jSpinner_remindMinutes.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        jSpinner_remindBefore_hour.setModel(new SpinnerNumberModel(0, 0, 23, 1));

        jLabel_RemindBefore_minutes = new JLabel("minutes");
        jLabel_remindHours = new JLabel("hours");

        jScrollPane1.setViewportView(jTextArea_descriprion);
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton_create_or_set, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton_cancel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(jLabel_planned_date)
                                                                .addGap(16, 16, 16)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jSpinner_remindBefore_hour, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(4, 4, 4)
                                                                                .addComponent(jLabel_remindHours)
                                                                                .addGap(14, 14, 14)
                                                                                .addComponent(jSpinner_remindMinutes, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(jDateChooser_PlannedDate, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(6, 6, 6))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel_remind_for)
                                                                        .addComponent(JTabe_nameTask))
                                                                .addGap(159, 159, 159)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField_name)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel_RemindBefore_minutes)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(jLabel1_before_the))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jSpinner_plannedHour, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(4, 4, 4)
                                                                .addComponent(jLabel_Hours)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jSpinner_plannedMin, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(4, 4, 4)
                                                                .addComponent(jLabel_minutes))))
                                        .addComponent(jLabel_dics))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(JTabe_nameTask))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jDateChooser_PlannedDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_planned_date)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jSpinner_plannedHour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_Hours)
                                                .addComponent(jSpinner_plannedMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_minutes)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_remind_for)
                                        .addComponent(jLabel1_before_the)
                                        .addComponent(jSpinner_remindBefore_hour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_remindHours)
                                        .addComponent(jSpinner_remindMinutes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_RemindBefore_minutes))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_dics)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_create_or_set)
                                        .addComponent(jButton_cancel))
                                .addContainerGap())
        );
        pack();
    }

    private void initComponentsEditTask() {

        setSize(new Dimension(424, 343));
        setResizable(false);

        setLocationRelativeTo(null); //установка по центру экрана

        JTabe_nameTask = new JLabel("Task name:");
        jTextField_name = new JTextField();

        jLabel_planned_date = new JLabel("Planned date:");
        jDateChooser_PlannedDate = new com.toedter.calendar.JDateChooser();
        jDateChooser_PlannedDate.setLocale(Locale.ENGLISH);
        jDateChooser_PlannedDate.setMaxSelectableDate(new Date(253370840399000L));//max data
        jDateChooser_PlannedDate.setMinSelectableDate(new Date(System.currentTimeMillis()));

        jDateChooser_notificationDate = new com.toedter.calendar.JDateChooser();
        jDateChooser_notificationDate.setLocale(Locale.ENGLISH);
        jDateChooser_notificationDate.setMaxSelectableDate(new Date(253370840399000L));//max data
        jDateChooser_notificationDate.setMinSelectableDate(new Date(System.currentTimeMillis()));

        jLabel_Hours = new JLabel("Hours");
        jLabel_minutes = new JLabel("Minutes");
        jLabel_notifMinutes = new JLabel("Minutes");
        jLable_notifhour = new JLabel("Hours");
        jLabel_notification_date = new JLabel("Notification date:");

        jLabel_dics = new JLabel("Description:");

        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();

        jTextArea_descriprion = new JTextArea();
        jTextArea_descriprion.setColumns(30);
        jTextArea_descriprion.setFont(new Font("Tahoma", 0, 12));
        jTextArea_descriprion.setRows(5);

        jButton_create_or_set = new JButton();
        jButton_create_or_set.setText("Save changes");
        jButton_cancel = new JButton();
        jButton_cancel.setText("Cancel");
        jButton_TaskCompleted = new JButton();
        jButton_TaskCompleted.setText("Complete task");
        jButton_CancelTask = new JButton();
        jButton_CancelTask.setText("Cancel task");

        jLabel_status = new JLabel("Status: ");
        jLabel_status.setFont(new Font("Tahoma", 0, 12));
        jLabel_status.setForeground(new Color(0, 0, 255));

        jSpinner_plannedMin = new JSpinner();
        jSpinner_plannedHour = new JSpinner();
        jSpinner_plannedHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_plannedMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        jSpinner_notifMinutes = new JSpinner();
        jSpinner_notifHour = new JSpinner();
        jSpinner_notifHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_notifMinutes.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        jLabel_statusInfo = new JLabel("");

        jScrollPane1.setViewportView(jTextArea_descriprion);
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton_create_or_set)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton_cancel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel_status)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel_statusInfo)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton_CancelTask)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton_TaskCompleted))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel_planned_date)
                                                                        .addComponent(jLabel_notification_date))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(jDateChooser_notificationDate, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                                                        .addComponent(jDateChooser_PlannedDate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(JTabe_nameTask)
                                                                .addGap(167, 167, 167)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jTextField_name)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jSpinner_plannedHour, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                                                        .addComponent(jSpinner_notifHour))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jLabel_Hours, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(jLable_notifhour, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jSpinner_notifMinutes)
                                                                        .addComponent(jSpinner_plannedMin, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel_minutes)
                                                                        .addComponent(jLabel_notifMinutes, GroupLayout.Alignment.TRAILING))))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(JTabe_nameTask)
                                        .addComponent(jTextField_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jSpinner_plannedHour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_Hours)
                                                .addComponent(jSpinner_plannedMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_minutes))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(jDateChooser_PlannedDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_planned_date)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_notification_date)
                                        .addComponent(jDateChooser_notificationDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(jSpinner_notifHour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLable_notifhour)
                                                .addComponent(jSpinner_notifMinutes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_notifMinutes)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_status)
                                        .addComponent(jLabel_statusInfo)
                                        .addComponent(jButton_CancelTask)
                                        .addComponent(jButton_TaskCompleted))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_create_or_set)
                                        .addComponent(jButton_cancel))
                                .addContainerGap())
        );
        pack();
    }

    private Task createTask() {
        if (checkTask()) {
            calcPlannedDate(); //считаем и заносим в поля
            //вычисляем дату нотификации
            int before_h = 0, before_m = 0;
            before_h = (int) this.jSpinner_remindBefore_hour.getValue();
            before_m = (int) this.jSpinner_remindMinutes.getValue();
            this.notificationDate = new Date((this.plannedDate.getTime() - (before_h * 60 * 60 * 1000) - (before_m * 60 * 1000)));

            if (this.plannedDate.before(Calendar.getInstance().getTime())) {
                JOptionPane.showMessageDialog(this, "The planned time has passed!", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            if (this.notificationDate.before(Calendar.getInstance().getTime())) {
                JOptionPane.showMessageDialog(this, "Task you intended to add has incorrect notification time!", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return TaskFactory.createTask(this.jTextField_name.getText().toString(), TaskStatus.Planned, this.jTextArea_descriprion.getText(), this.notificationDate, this.plannedDate);
        }
        return null;
    }

    private boolean saveTask() {
        if (checkTask()) {
            if ((loadTask.getStatus() == TaskStatus.Cancelled) || (loadTask.getStatus() == TaskStatus.Completed)) {
                this.loadTask.setName(jTextField_name.getText().toString());
                this.loadTask.setDescription(this.jTextArea_descriprion.getText().toString());
            } else {
                this.loadTask.setName(jTextField_name.getText().toString());
                this.loadTask.setDescription(this.jTextArea_descriprion.getText().toString());
                this.calcPlannedDate();
                this.calcNotificationDate();

                if (this.notificationDate.before(Calendar.getInstance().getTime())) {
                    JOptionPane.showMessageDialog(this, "Task you intended to add has incorrect notification time!", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                this.loadTask.setPlannedDate(this.plannedDate);
                this.loadTask.setNotificationDate(this.notificationDate);
            }
            return true;
        }
        return false;
    }

    private boolean checkTask() {
        if (this.jTextField_name.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Enter task name!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.jDateChooser_PlannedDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Enter planned date!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void calcNotificationDate() {
        int notif_h, notif_m;
        Calendar calend = Calendar.getInstance();
        notif_h = (int) this.jSpinner_notifHour.getValue();
        notif_m = (int) this.jSpinner_notifMinutes.getValue();
        Date notif = this.jDateChooser_notificationDate.getDate();
        calend.setTime(notif);
        calend.set(Calendar.HOUR_OF_DAY, notif_h);
        calend.set(Calendar.MINUTE, notif_m);
        calend.set(Calendar.SECOND, 0);
        calend.set(Calendar.MILLISECOND, 0);
        notif = calend.getTime();
        this.notificationDate = notif;
    }

    private void paintTask(Task task) {
        this.jTextField_name.setText(task.getName());
        this.jDateChooser_PlannedDate.setDate(task.getPlannedDate());
        this.jDateChooser_notificationDate.setDate(task.getNotificationDate());

        Calendar calend = Calendar.getInstance();
        calend.setTime(task.getPlannedDate());
        this.jSpinner_plannedHour.setValue(calend.get(Calendar.HOUR_OF_DAY));
        this.jSpinner_plannedMin.setValue(calend.get(Calendar.MINUTE));
        this.jTextArea_descriprion.setText(task.getDescription());

        calend.setTime(task.getNotificationDate());
        this.jSpinner_notifHour.setValue(calend.get(Calendar.HOUR_OF_DAY));
        this.jSpinner_notifMinutes.setValue(calend.get(Calendar.MINUTE));
        this.jLabel_status.setText("Status: " + task.getStatus());
        if ((loadTask.getStatus() == TaskStatus.Cancelled) || (loadTask.getStatus() == TaskStatus.Completed)) {
            this.jDateChooser_notificationDate.setEnabled(false);
            this.jDateChooser_PlannedDate.setEnabled(false);
            this.jSpinner_notifMinutes.setEnabled(false);
            this.jSpinner_notifHour.setEnabled(false);
            this.jSpinner_plannedMin.setEnabled(false);
            this.jSpinner_plannedHour.setEnabled(false);
            this.jButton_CancelTask.setEnabled(false);
            this.jButton_TaskCompleted.setEnabled(false);
        }
    }

    public void calcPlannedDate() {
        int h, m;

        h = (int) this.jSpinner_plannedHour.getValue();
        m = (int) this.jSpinner_plannedMin.getValue();

        Date plan = this.jDateChooser_PlannedDate.getDate(); //Получаем дату (время не верное)
        Calendar calend = Calendar.getInstance(); //Создание даты уведомления (перерасчет через Calendar)
        calend.setTime(plan);
        calend.set(Calendar.HOUR_OF_DAY, h);
        calend.set(Calendar.MINUTE, m);
        calend.set(Calendar.SECOND, 0);
        calend.set(Calendar.MILLISECOND, 0);
        plan = calend.getTime();
        this.plannedDate = plan;
    }

    private void mainFormAddTask(Task newTask) {
        if (clientFacade.getDataOutputStream() == null)
            JOptionPane.showMessageDialog(null,
                    "Server is not available! Try later!", "Error", JOptionPane.ERROR_MESSAGE);
        else
            try {
                commandSender.sendAddCommand(newTask, clientFacade.getDataOutputStream());
            } catch (UnsuccessfulCommandActionException e) {
                messageBox.showAskForRestartMessage();
            }
    }

    private void mainFormEditTask(Task taskSet) {
        if (clientFacade.getDataOutputStream() == null)
            JOptionPane.showMessageDialog(null,
                    "Server is not available! Try later!", "Error", JOptionPane.ERROR_MESSAGE);
        else {
            if ((loadTask.getStatus() != TaskStatus.Completed) && (loadTask.getStatus() != TaskStatus.Cancelled)) {
                taskSet.setStatus(TaskStatus.Rescheduled);
                try {
                    commandSender.sendEditCommand(taskSet, clientFacade.getDataOutputStream());
                } catch (UnsuccessfulCommandActionException e) {
                    messageBox.showAskForRestartMessage();
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "You can not reschedule cancelled or completed task!",
                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
}