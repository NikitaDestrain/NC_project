package server.gui.mainform;

import server.network.ServerProcessor;
import server.properties.ParserProperties;
import auxiliaryclasses.ConstantsClass;
import server.controller.*;
import server.exceptions.IllegalPropertyException;
import server.gui.taskwindow.TaskWindow;
import server.model.Journal;
import server.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MainForm extends JFrame {
    /*
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
    private Controller controller = Controller.getInstance();

    public MainForm() {
        super("Task Scheduler");
        instance = this;
        fileChooser = new JFileChooser();
        journalBackup = new XMLSerializer();
        Journal backup = Controller.getInstance().getJournal();
        this.journal = backup == null ? new Journal() : backup;
        try {
            icon = new ImageIcon(ParserProperties.getInstance().getProperty(ConstantsClass.MAIN_FORM_ICON));
        } catch (IllegalPropertyException e) {
            JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupted or missing!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        tablePanel = new TablePanel(this);
        tablePanel.setData(this.journal.getTasks());
        tablePanel.refresh();
        buttonPanel = new ButtonPanel(this);
        buttonPanel.setJtable((tablePanel.getTable()));

        buttonPanel.setTableListener(new TableListener() {
            private StringBuilder builder = new StringBuilder("");

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
                                if (!builder.toString().equals(""))
                                    controller.removeTask(builder.toString());
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
            private StringBuilder builder = new StringBuilder("");

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
                                if (!builder.toString().equals(""))
                                    controller.removeTask(builder.toString());
                                break;
                        }
                    }
                });
            }
        });

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
                        ServerProcessor.getInstance().finishServer();
                        String path = ParserProperties.getInstance().getProperty(ConstantsClass.JOURNAL_XML_FILE);
                        if (path == null)
                            JOptionPane.showMessageDialog(MainForm.this,
                                    "Incorrect file path",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        else {
                            journalBackup.writeJournal(journal, path);
                            UserAuthorizer.getInstance().writeUserData(ParserProperties.getInstance()
                                    .getProperty(ConstantsClass.USER_DATA));
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

    /**
     * Sets journal to be represented at this <code>mainform</code>
     *
     * @param journal object with tasks for representation
     */
    /*public void setJournal(Journal journal) {
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
        this.journal = controller.getJournal();
        tablePanel.setData(this.journal.getTasks());
        buttonPanel.setJtable((tablePanel.getTable()));
        tablePanel.refresh();
    }*/
}
