package GUI.TaskWindow;

import GUI.MainForm.MainForm;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class TaskWindow extends JFrame {

    private MainForm owner;
    private Task loadTask;
    private JLabel JTabe_name;
    private JLabel jLabel_cont;
    private JLabel jLabel_date;
    private JLabel jLabel_dics;
    private JLabel jLabel_notif;
    private JLabel jLabel_time;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea_descriprion;
    private JLabel jLabel_status;
    private JTextField jTextField_contacts;
    private JTextField jTextField_name;
    private JTextField jTextField_notification_date; //высчитать вермя напоминания
    private JButton jButton_cancel;
    private JButton jButton_create_or_set;
    private JSpinner jSpinner_clock_hour;
    private JSpinner jSpinner_clock_min;
    private JLabel jLabel_cloc2;
    private MainForm main;

    private com.toedter.calendar.JDateChooser jDateChooser; //Подключить библиотеку кину в telegram

    //Перегруженные конструкторы для создания и просмотра/редактирования
    public TaskWindow(MainForm owner) {

        super("Create new task");

        this.initComponents_WindowTask();
        this.owner = owner;
        //setVisible(true);
        this.jButton_create_or_set.setText("Create");
        this.jButton_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });
        //создаем новую таску
        this.jButton_create_or_set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Task task1 = createTask();
                if (task1 != null) {
                    main = MainForm.getInstance();
                    main.addTask(task1);
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
        setResizable(false);
        setSize(new java.awt.Dimension(350, 400));
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
                    dispose();
                }

            }
        });
        setVisible(true);
    }

    private void initComponents_WindowTask() {
        setResizable(false);
        setSize(new java.awt.Dimension(350, 400));
        setLocationRelativeTo(null); //установка по центру экрана

        jDateChooser = new com.toedter.calendar.JDateChooser();

        jDateChooser.setMaxSelectableDate(new java.util.Date(253370840399000L));//max min data
        jDateChooser.setMinSelectableDate(new java.util.Date(-62135780400000L));

        JTabe_name = new JLabel("Task name:");
        jTextField_name = new JTextField();
        jLabel_date = new JLabel("Date:");
        jLabel_notif = new JLabel("Notification time:");
        jTextField_notification_date = new JTextField();
        jLabel_cont = new JLabel("Contacts:");
        jTextField_contacts = new JTextField();
        jLabel_dics = new JLabel("Description:");
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTextArea_descriprion = new JTextArea();
        jButton_create_or_set = new JButton();
        jButton_cancel = new JButton();
        jButton_create_or_set.setText("");
        jButton_cancel.setText("Cancel");

        this.jLabel_status = new JLabel();
        jLabel_status.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel_status.setForeground(new java.awt.Color(0, 0, 153));
        jLabel_status.setText("Status:");

        jTextArea_descriprion.setColumns(30);
        jTextArea_descriprion.setFont(new Font("Tahoma", 0, 12));
        jTextArea_descriprion.setRows(5);
        jSpinner_clock_min = new JSpinner();
        jSpinner_clock_hour = new JSpinner();
        jSpinner_clock_hour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_clock_min.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        jLabel_cloc2 = new JLabel();
        jLabel_cloc2.setText(":");

        jScrollPane1.setViewportView(jTextArea_descriprion);

        jPanel1.add(jScrollPane1, BorderLayout.CENTER);


        //РАЗМЕЩЕНИЕ конопое НЕ ТРОГАТЬ
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 323, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 4, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton_create_or_set, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jButton_cancel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel_cont)
                                                                        .addComponent(jLabel_dics)
                                                                        .addComponent(jLabel_notif))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jSpinner_clock_hour, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jLabel_cloc2)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jSpinner_clock_min, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(6, 6, 6))
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel_status)
                                                                                .addComponent(jTextField_contacts, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(JTabe_name, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel_date))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jTextField_name, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                                                        .addComponent(jDateChooser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(JTabe_name)
                                        .addComponent(jTextField_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel_date)
                                        .addComponent(jDateChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_notif)
                                        .addComponent(jLabel_cloc2)
                                        .addComponent(jSpinner_clock_min, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSpinner_clock_hour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField_contacts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_cont))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_dics)
                                        .addComponent(jLabel_status))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton_create_or_set)
                                        .addComponent(jButton_cancel))
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }

    private Task createTask() //для создания task по введенным значениям
    {
        if (checkTask()) //проверка на валидность
        {
            Date d = this.getNotificationDate();
            Task newTask = new Task(this.JTabe_name.getText().toString(), TaskStatus.Planned, this.jTextArea_descriprion.getText(), d, d);
            return newTask;
        }
        return null;
    }

    private boolean saveTask() //СОхранение измененной задачи 
    {
        if (checkTask()) //проверка на валидность
        {
            this.loadTask.setName(jTextField_name.getText().toString());
            this.loadTask.setDescription(this.jTextArea_descriprion.getText().toString());
            this.loadTask.setNotificationDate(this.getNotificationDate());
            this.loadTask.setStatus(TaskStatus.Planned);
            main = MainForm.getInstance();
            main.editTask(loadTask.getId(), loadTask);
            return true;
        }
        return false;
    }

    private boolean checkTask()// проверка заполнения необходимых полей
    {
        StringBuffer err = new StringBuffer("");

        if (this.jTextField_name.getText().length() == 0) {
            err.append("Enter tak name ");
            new ErrDialog(this, err.toString());
            return false;

        }
        if (this.jDateChooser.getDate() == null) {
            err.append("Enter date ");
            new ErrDialog(this, err.toString());
            return false;
        }

        return true;
    }

    private Date getNotificationDate() //высчитываетяс конечное время с учетом дня месяца года времени минут
    {
        int h = 0, m = 0;

        h = (int) this.jSpinner_clock_hour.getValue();
        m = (int) this.jSpinner_clock_min.getValue();

        Date d = this.jDateChooser.getDate(); //Получаем дату только время текущее
        Calendar calend = Calendar.getInstance(); //Создание даты уведомления (перерасчет через Calendar)
        calend.setTime(d);
        calend.set(Calendar.HOUR_OF_DAY, h);
        calend.set(Calendar.MINUTE, m);
        calend.set(Calendar.SECOND, 0);
        d = calend.getTime();
        return d;
    }

    private void paintTask(Task task)//заполняет поля формы
    {
        this.jTextField_name.setText(task.getName());
        this.jDateChooser.setDate(task.getNotificationDate());

        Calendar calend = Calendar.getInstance();
        calend.setTime(task.getNotificationDate());
        this.jSpinner_clock_hour.setValue((int) calend.get(Calendar.HOUR_OF_DAY));
        this.jSpinner_clock_min.setValue((int) calend.get(Calendar.MINUTE));
        this.jTextArea_descriprion.setText(task.getDescription());
        this.jLabel_status.setText("Status:" + task.getStatus());
    }

    /*   private void MainForm_addTask( Task newTask ) //вызываю методы MAINform для записи изменений
    {
        owner.addTask(newTask);
    }
    private void MainForm_setTask(Task TaskSet)
    {
        owner.setTask(TaskSet);
    }
     */
}
