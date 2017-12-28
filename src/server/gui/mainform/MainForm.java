package server.gui.mainform;

import server.controller.*;
import server.exceptions.IllegalPropertyException;
import server.gui.taskwindow.TaskWindow;
import server.model.Journal;
import server.model.Task;
import server.properties.ParserProperties;
import server.properties.PropertiesConstant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainForm extends JFrame {
    private JFileChooser fileChooser;
    private XMLSerializer journalBackup;
    private Journal journal;
    private TablePanel tablePanel;
    private ButtonPanel buttonPanel;
    private TrayIcon tray;
    private ImageIcon icon;
    private SystemTray systemTray = SystemTray.getSystemTray();
    private static MainForm instance;
    private TaskSender taskSender = TaskSender.getInstance();
    private JLabel usernameLabel = new JLabel("Your status is Server. You logged as: ");

    public MainForm() {
        super("Task Scheduler");
        instance = this;
        fileChooser = new JFileChooser();
        journalBackup = new XMLSerializer();
        Journal backup = Controller.getInstance().getJournal();
        this.journal = backup == null ? new Journal() : backup ;
        try {
            icon = new ImageIcon(ParserProperties.getInstance().getProperties(PropertiesConstant.MAIN_FORM_ICON.toString()));
        } catch (IllegalPropertyException e) {
            JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        tablePanel = new TablePanel(this);
        tablePanel.setData(this.journal.getTasks());
        tablePanel.refresh();
        buttonPanel = new ButtonPanel(this);
        buttonPanel.setJtable((tablePanel.getTable()));

        buttonPanel.setTableListener(new TableListener() {
            private List<Integer> tasksToDelete = new ArrayList<>();
            private StringBuilder builder = new StringBuilder();
            @Override
            public void rowDeleted(Integer... rows) {
                buttonPanel.setListener(new TaskActionListener() {
                    @Override
                    public void setTask(int action, Task task) {
                        if (task != null && action == TaskActionListener.EDIT_TASK) {
                            new TaskWindow(MainForm.this, task);
                        }
                    }

                    @Override
                    public void setAction(int action) {
                        switch (action) {
                            case TaskActionListener.ADD_TASK:
                                new TaskWindow(MainForm.this);
                                break;
                            case TaskActionListener.DELETE_TASK:
                                taskSender.clearTask();
                                for (int i = 0; i < rows.length; i++) {
                                    Task task = journal.getTasks().get(rows[i]);
                                    builder.append(task.getId());
                                    builder.append(",");
                                }
                                Controller.getInstance().removeTask(builder.toString());
                                break;
                        }
                    }
                });
            }
        });

        buttonPanel.setListener(new TaskActionListener() {
            @Override
            public void setTask(int action, Task task) {
                if (task != null && action == TaskActionListener.EDIT_TASK) {
                    new TaskWindow(MainForm.this, task);
                }
            }

            @Override
            public void setAction(int action) {
                switch (action) {
                    case TaskActionListener.ADD_TASK:
                        new TaskWindow(MainForm.this);
                        break;
                }
            }
        });

        tablePanel.setTableListener(new TableListener() {
            private List<Integer> tasksToDelete = new LinkedList<>();
            private StringBuilder builder = new StringBuilder();
            @Override
            public void rowDeleted(Integer... rows) {
                buttonPanel.setListener(new TaskActionListener() {
                    @Override
                    public void setTask(int action, Task task) {
                        if (task != null && action == TaskActionListener.EDIT_TASK) {
                            new TaskWindow(MainForm.this, task);
                        }
                    }

                    @Override
                    public void setAction(int action) {
                        switch (action) {
                            case TaskActionListener.ADD_TASK:
                                new TaskWindow(MainForm.this);
                                break;
                            case TaskActionListener.DELETE_TASK:
                                taskSender.clearTask();
                                for (int i = 0; i < rows.length; i++) {
                                    Task task = journal.getTasks().get(rows[i]);
                                    builder.append(task.getId());
                                    builder.append(",");
                                }
                                Controller.getInstance().removeTask(builder.toString());
                                break;
                        }
                    }
                });
            }
        });

        setJMenuBar(createMenu());
        setLayout(new BorderLayout());
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(usernameLabel, BorderLayout.NORTH);
        tray = new TrayIcon(icon.getImage());
        tray.addActionListener((ActionEvent e) -> {
            setVisible(true);
            setState(JFrame.NORMAL);
            removeFromTray();
        });
        tray.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        tray.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                tray.setToolTip("Double click to show");
            }
        });

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.ICONIFIED) {
                    setVisible(false);
                    addToTray();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int action = JOptionPane.showConfirmDialog(
                        MainForm.this, "Do you really want to close the app?",
                        "Warning!",
                        JOptionPane.YES_NO_OPTION);

                if (action == JOptionPane.OK_OPTION) {
                    try {
                        String path = ParserProperties.getInstance().getProperties(PropertiesConstant.XML_FILE.toString());
                        if (path == null)
                            JOptionPane.showMessageDialog(MainForm.this,
                                    "Incorrect file path",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        else {
                            journalBackup.writeJournal(journal, path);
                            Controller.getInstance().writeUserData(ParserProperties.getInstance()
                                    .getProperties("USER_DATA"));
                            // todo finish ServerNetworkFacade.getInstance().finish();
                            System.exit(0);
                        }
                    } catch (IllegalPropertyException ex) {
                        JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(MainForm.this,
                                "Could not save journal to file",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setSize(800, 500);
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(false);
    }

    private void removeFromTray() {
        systemTray.remove(tray);
    }

    private void addToTray() {
        try {
            systemTray.add(tray);
            tray.displayMessage("Attention!", "Task scheduler was hidden to tray", TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        if (username != null)
            usernameLabel.setText(usernameLabel.getText() + username);
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();

        /*JMenu fileMenu = new JMenu("File");
        menu.add(fileMenu);

        JMenuItem exportJournal = new JMenuItem("Export journal...");//todo эти пункты сейчас ничего полезного не делают
        JMenuItem importJournal = new JMenuItem("Import journal...");
        JMenuItem exit = new JMenuItem("Exit");

        fileMenu.add(exportJournal);
        fileMenu.add(importJournal);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        exit.addActionListener((ActionEvent e) -> {
            int action = JOptionPane.showConfirmDialog(
                    MainForm.this, "Do you really want to close the app?",
                    "Warning!",
                    JOptionPane.YES_NO_OPTION);//todo No и Cancel делаю ровно то же самое.

            if (action == JOptionPane.OK_OPTION) {
                try {
                    journalBackup.writeJournal(this.journal, ParserProperties.getInstance().getProperties("XML_FILE"));//todo захардкоженные значения стоит выносить в константы
                } catch (IllegalPropertyException ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file ",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
            }
        });

        exportJournal.addActionListener((ActionEvent e) -> {

            try {
                journalBackup.writeJournal(journal, ParserProperties.getInstance().getProperties("XML_FILE"));
            } catch (IllegalPropertyException ex) {
                JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        importJournal.addActionListener((ActionEvent e) -> {
            try {
                this.journal = journalBackup.readJournal(ParserProperties.getInstance().getProperties("PATH_TO_JOURNAL"));
                if (this.journal != null) {
                    Controller.getInstance().setJournal(this.journal);
                    tablePanel.setData(journal.getTasks());
                    tablePanel.refresh();
                } else
                    JOptionPane.showMessageDialog(MainForm.this,
                            "Could not load journal from file",
                            "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalPropertyException ex) {
                JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainForm.this, "Could not load journal from file",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });*/

        return menu;
    }

    /**
     * Sets journal to be represented at this <code>mainform</code>
     *
     * @param journal object with tasks for representation
     */
    public void setJournal(Journal journal) {
        if (journal != null) {
            Controller controller = Controller.getInstance();
            this.journal = controller.getJournal();
            tablePanel.setData(this.journal.getTasks());
            tablePanel.refresh();
        }
    }

    public static MainForm getInstance() {
        return instance;
    }

    public void updateJournal() {
        //server.exceptions.controller = Controller.getInstance();//todo нет нужны каждый раз перезаписывать поле. Синглетон на то и синглетон, что он всегда один и тот же.
        // Можно или один раз инициализировать поле или вообще отказаться от поля и каждый раз просто вызывать Controller.getInstance()
        Controller controller = Controller.getInstance();
        this.journal = controller.getJournal();
        tablePanel.setData(this.journal.getTasks());
        buttonPanel.setJtable((tablePanel.getTable()));
        tablePanel.refresh();
    }
}
