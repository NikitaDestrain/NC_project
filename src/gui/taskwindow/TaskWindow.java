package gui.taskwindow;

import com.toedter.calendar.JDateChooser;
import controller.Controller;
import gui.mainform.MainForm;
import model.Task;
import model.TaskStatus;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskWindow extends JFrame {

    private MainForm owner;
    private Controller controller;
    private Task loadTask;

    private JLabel JTabe_nameTask;
    private JButton jButton_cancel;
    private JButton jButton_create_or_set;
    private JComboBox<String> jComboBox_changeStatus;
    private com.toedter.calendar.JDateChooser jDateChooser_PlannedDate;
    private com.toedter.calendar.JDateChooser jDateChooser_notificationDate;
    private JLabel jLabel_Hours;
    private JLabel jLabel_dics;
    private javax.swing.JLabel jLabel_minutes;
    private javax.swing.JLabel jLabel_notifMinutes;
    private javax.swing.JLabel jLabel_notification_date;
    private javax.swing.JLabel jLabel_planned_date;
    private javax.swing.JLabel jLabel_status;
    private javax.swing.JLabel jLabel_statusInfo;
    private javax.swing.JLabel jLable_jangeStatus;
    private javax.swing.JLabel jLable_notifhour;

    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSpinner jSpinner_plannedHour;
    private javax.swing.JSpinner jSpinner_plannedMin;
    private javax.swing.JSpinner jSpinner_notifHour;
    private javax.swing.JSpinner jSpinner_notifMinutes;
    private javax.swing.JTextArea jTextArea_descriprion;
    private javax.swing.JTextField jTextField_name;


    private Date plannedDate;//запланированное время
    private Date notificationDate; //время уведомления

    //Перегруженные конструкторы для создания и просмотра/редактирования
    public TaskWindow(MainForm owner) {

        super("Create new task");
        this.controller = Controller.getInstance();
        this.initComponents_WindowTask();
        this.owner = owner;

        this.jLabel_status.setVisible(false);
        this.jComboBox_changeStatus.setVisible(false);
        this.jLable_jangeStatus.setVisible(false);
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
        this.controller = Controller.getInstance();

        setLocationRelativeTo(null); //установка по центру экрана

        this.initComponents_WindowTask();
        this.jButton_create_or_set.setText("Set change");
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
        setVisible(true);
    }

    private void initComponents_WindowTask() {


        setSize(new java.awt.Dimension(424, 343));
        setResizable(false);

        setLocationRelativeTo(null); //установка по центру экрана

        JTabe_nameTask = new JLabel("Task name:");
        jTextField_name = new JTextField();

        jLabel_planned_date = new JLabel("Planned date:");
        jDateChooser_PlannedDate = new com.toedter.calendar.JDateChooser();
        jDateChooser_PlannedDate.setLocale(Locale.ENGLISH);
        jDateChooser_PlannedDate.setMaxSelectableDate(new java.util.Date(253370840399000L));//max data
        jDateChooser_PlannedDate.setMinSelectableDate(new java.util.Date(System.currentTimeMillis()));

        jDateChooser_notificationDate = new com.toedter.calendar.JDateChooser();
        jDateChooser_notificationDate.setLocale(Locale.ENGLISH);
        jDateChooser_notificationDate.setMaxSelectableDate(new java.util.Date(253370840399000L));//max data
        jDateChooser_notificationDate.setMinSelectableDate(new java.util.Date(System.currentTimeMillis()));

        jComboBox_changeStatus = new JComboBox<>();
        jComboBox_changeStatus.setModel(new DefaultComboBoxModel<>(new String[]{"No", "Completed", "Cancel"}));

        jLabel_Hours = new javax.swing.JLabel("Hours");
        jLabel_minutes = new javax.swing.JLabel("Minutes");
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
        jButton_cancel = new JButton();
        jButton_cancel.setText("Cancel");


        jLabel_status = new JLabel("Status: ");
        jLabel_status.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel_status.setForeground(new java.awt.Color(0, 0, 255));


        jSpinner_plannedMin = new JSpinner();
        jSpinner_plannedHour = new JSpinner();
        jSpinner_plannedHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_plannedMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        jSpinner_notifMinutes = new JSpinner();
        jSpinner_notifHour = new JSpinner();
        jSpinner_notifHour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_notifMinutes.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        jLable_jangeStatus = new JLabel("Change status?");
        jLabel_statusInfo = new JLabel("");


        jScrollPane1.setViewportView(jTextArea_descriprion);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        );


        //РАЗМЕЩЕНИЕ конопок НЕ ТРОГАТЬ
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButton_create_or_set, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(JTabe_nameTask)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jTextField_name, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel_notification_date, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel_dics)
                                                        .addComponent(jLabel_planned_date, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(jLabel_statusInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel_status)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLable_jangeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBox_changeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jDateChooser_PlannedDate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jDateChooser_notificationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jSpinner_notifHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(4, 4, 4)
                                                                                .addComponent(jLable_notifhour)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jSpinner_notifMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(4, 4, 4)
                                                                                .addComponent(jLabel_notifMinutes))
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jSpinner_plannedHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(4, 4, 4)
                                                                                .addComponent(jLabel_Hours)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jSpinner_plannedMin, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(4, 4, 4)
                                                                                .addComponent(jLabel_minutes)))))))
                                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(JTabe_nameTask)
                                        .addComponent(jTextField_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jSpinner_plannedMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_minutes)
                                                .addComponent(jLabel_Hours)
                                                .addComponent(jSpinner_plannedHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel_planned_date))
                                        .addComponent(jDateChooser_PlannedDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_notification_date, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLable_notifhour)
                                                .addComponent(jSpinner_notifMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_notifMinutes)
                                                .addComponent(jSpinner_notifHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jDateChooser_notificationDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel_statusInfo)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel_dics)
                                                        .addComponent(jLabel_status)
                                                        .addComponent(jLable_jangeStatus)
                                                        .addComponent(jComboBox_changeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(22, 22, 22)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton_create_or_set)
                                        .addComponent(jButton_cancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }


    private Task createTask() //для создания task по введенным значениям
    {
        if (checkTask()) //проверка на валидность
        {
            calcPlannedDate(); //считаем и заносим в поля
            calcNotificationDate();

            Task task = new Task(this.jTextField_name.getText().toString(), TaskStatus.Planned, this.jTextArea_descriprion.getText(), this.notificationDate, this.plannedDate);

            return task;
        }
        return null;
    }

    private boolean saveTask() //СОхранение измененной задачи 
    {
        if (checkTask()) //проверка на валидность
        {
            this.loadTask.setName(jTextField_name.getText().toString());
            this.loadTask.setDescription(this.jTextArea_descriprion.getText().toString());
            this.calcPlannedDate();
            this.calcNotificationDate();
            System.out.println(this.plannedDate + " " + this.notificationDate + " ");
            this.loadTask.setPlannedDate(this.plannedDate);
            this.loadTask.setNotificationDate(this.notificationDate);
            String new_status = this.jComboBox_changeStatus.getSelectedItem().toString();
            if (new_status.equals("Cancel")) {
                loadTask.setStatus(TaskStatus.Cancelled);
            }
            if (new_status.equals("Completed")) {
                loadTask.setStatus(TaskStatus.Completed);
            }


            return true;
        }


        return false;
    }

    private boolean checkTask()// проверка заполнения необходимых полей
    {
        StringBuffer err = new StringBuffer("");

        if (this.jTextField_name.getText().length() == 0) {
            err.append("Enter task name ");
            new ErrDialog(this, err.toString());
            return false;

        }
        if (this.jDateChooser_PlannedDate.getDate() == null) {
            err.append("Enter planned date ");
            new ErrDialog(this, err.toString());
            return false;
        }
        if (this.jDateChooser_notificationDate.getDate() == null) {
            err.append("Enter notification date ");
            new ErrDialog(this, err.toString());
            return false;
        }


        return true;
    }

    private void calcNotificationDate() //высчитываетяс конечное время с учетом времени оповещения
    {
        //высчит дату уведомления
        int notif_h = 0, notif_m = 0;
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

    private void paintTask(Task task)//заполняет поля формы
    {
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
    }

    public void calcPlannedDate() ///метод получения запланированной даты
    {
        int h = 0, m = 0;

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

    private void mainFormAddTask(Task newTask) //вызываю методы MAINform для записи изменений
    {

        controller.addTask(newTask);
        owner.updateJournal();
    }

    private void mainFormEditTask(Task taskSet) {

        taskSet.setStatus(TaskStatus.Rescheduled);
        controller.editTask(this.loadTask.getId(), taskSet);
        owner.updateJournal();
    }

}
