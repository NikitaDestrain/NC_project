package gui.taskwindow;

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

    private Task loadTask;
    private JLabel JTabe_nameTask;
    private JLabel jLabel_date;
    private JLabel jLabel_dics;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea_descriprion;
    private JLabel jLabel_status;
    private JTextField jTextField_name;
    private JButton jButton_cancel;
    private JButton jButton_create_or_set;
    private JSpinner jSpinner_clock_hour;
    private JSpinner jSpinner_clock_min;

    private JLabel jLabel_Hours;
    private JLabel jLabel_minutes;
    private JLabel jLabel_remiend;

    private javax.swing.JComboBox<String> jComboBox_remiendBefore;

    private com.toedter.calendar.JDateChooser jDateChooser; //Подключить библиотеку кину в telegram

    private Date plannedDate;//запланированное время
    private Date notificationDate; //время уведомления

    //Перегруженные конструкторы для создания и просмотра/редактирования
    public TaskWindow(MainForm owner) {

        super("Create new task");

        this.initComponents_WindowTask();
        this.owner = owner;

      this.jLabel_status.setVisible(false);
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

        setSize(new Dimension(350, 350));
        setResizable(false);

        setLocationRelativeTo(null); //установка по центру экрана



        jComboBox_remiendBefore = new javax.swing.JComboBox<>();
       jComboBox_remiendBefore.setModel(new DefaultComboBoxModel<>(new String[] { "1 minute", "5  minutes", "10  minutes", "15  minutes", "20  minutes", "25  minutes", "30  minutes", "45  minutes", "1 hour", "2 hours", "3 hours", "6 hours", "12 hours" }));



        JTabe_nameTask = new JLabel("Task name:");
        jTextField_name = new JTextField();

        jLabel_date = new JLabel("Date:");
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jDateChooser.setLocale(Locale.ENGLISH);
        jDateChooser.setMaxSelectableDate(new java.util.Date(253370840399000L));//max min data
        jDateChooser.setMinSelectableDate(new java.util.Date(-62135780400000L));

        jLabel_Hours = new javax.swing.JLabel("Hours");
        jLabel_minutes = new javax.swing.JLabel("Minutes");
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

        jLabel_remiend = new JLabel("Remind before:");

        jLabel_status = new JLabel();
        jLabel_status.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel_status.setForeground(new java.awt.Color(0, 0, 153));

        jLabel_status.setText("Status: ");


        jSpinner_clock_min = new JSpinner();
        jSpinner_clock_hour = new JSpinner();
        jSpinner_clock_hour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        jSpinner_clock_min.setModel(new SpinnerNumberModel(0, 0, 59, 1));



        jScrollPane1.setViewportView(jTextArea_descriprion);

        jPanel1.add(jScrollPane1, BorderLayout.CENTER);



        jComboBox_remiendBefore.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0 minutes", "1 minutes", "5 minutes", "10 minutes", "15 minutes", "20 minutes", "25 minutes", "30 minutes", "45 minutes", "1 hour", "2 hours", "3 hours", "6 hours", "12 hours" }));
        //РАЗМЕЩЕНИЕ конопое НЕ ТРОГАТЬ
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jScrollPane1)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jButton_create_or_set, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jButton_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(JTabe_nameTask, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel_date)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                                                                                .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(jLabel_remiend))
                                                                .addGap(10, 10, 10)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jTextField_name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jComboBox_remiendBefore, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel_status))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jSpinner_clock_hour, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jLabel_Hours)
                                                                                .addGap(16, 16, 16)
                                                                                .addComponent(jSpinner_clock_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(8, 8, 8)
                                                                                .addComponent(jLabel_minutes)))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_dics)
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(JTabe_nameTask)
                                        .addComponent(jTextField_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jSpinner_clock_hour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_Hours)
                                                .addComponent(jSpinner_clock_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_minutes))
                                        .addComponent(jLabel_date, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jComboBox_remiendBefore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_remiend))
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel_dics)
                                        .addComponent(jLabel_status, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton_create_or_set)
                                        .addComponent(jButton_cancel))
                                .addContainerGap(26, Short.MAX_VALUE))
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
            System.out.println(this.plannedDate +" "+this.notificationDate+" ");
            this.loadTask.setPlannedDate(this.plannedDate);
            this.loadTask.setNotificationDate(this.notificationDate);

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

    private void calcNotificationDate() //высчитываетяс конечное время с учетом времени оповещения
    {
        int h = 0, m = 0;




        String stringRaminedTime = (String) this.jComboBox_remiendBefore.getSelectedItem();

       String[] param =  stringRaminedTime.split(" "); //0 элемент кол-во 1-тап (мин или часы)

        if(param[1].contains("hour")) //проверка на наличие такой последовательности слова в стоке
        {
           h = Integer.parseInt(param[0]);
           // System.out.println("hours "+h);
        }
        if (param[1].contains("minut"))
        {
           m = Integer.parseInt(param[0]);
           // System.out.println("minutes "+m);
        }
        this.notificationDate = new Date((this.plannedDate.getTime() - (h*60*60*1000)-(m*60*1000)));
      ///  System.out.println(this.notificationDate);



    }

    private void paintTask(Task task)//заполняет поля формы
    {
        this.jTextField_name.setText(task.getName());
        this.jDateChooser.setDate(task.getNotificationDate());

        Calendar calend = Calendar.getInstance();
        calend.setTime(task.getPlannedDate());

        this.jSpinner_clock_hour.setValue((int) calend.get(Calendar.HOUR_OF_DAY));
        this.jSpinner_clock_min.setValue((int) calend.get(Calendar.MINUTE));
        this.jTextArea_descriprion.setText(task.getDescription());
        this.jLabel_status.setText("Status: " + task.getStatus());
    }

    public void calcPlannedDate() ///метод получения запланированной даты
    {
        int h = 0, m = 0;

        h = (int) this.jSpinner_clock_hour.getValue();
        m = (int) this.jSpinner_clock_min.getValue();

        Date d = this.jDateChooser.getDate(); //Получаем дату (время не верное)
        Calendar calend = Calendar.getInstance(); //Создание даты уведомления (перерасчет через Calendar)
        calend.setTime(d);
        calend.set(Calendar.HOUR_OF_DAY, h);
        calend.set(Calendar.MINUTE, m);
        calend.set(Calendar.SECOND, 0);
        calend.set(Calendar.MILLISECOND, 0);
        d = calend.getTime();
        this.plannedDate=d;


    }

      private void mainFormAddTask( Task newTask ) //вызываю методы MAINform для записи изменений
    {
        System.out.println(newTask);
       owner.addTask(newTask);

    }
    private void mainFormEditTask(Task taskSet)
    {

        owner.editTask(this.loadTask.getId(),taskSet);
    }

}
