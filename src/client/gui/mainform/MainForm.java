package client.gui.mainform;

import client.commandprocessor.ClientCommandSender;
import client.exceptions.UnsuccessfulCommandActionException;
import client.gui.taskwindow.TaskWindow;
import client.exceptions.IllegalPropertyException;
import server.model.Journal;
import server.model.Task;
import client.network.ClientNetworkFacade;
import client.properties.ParserProperties;
import auxiliaryclasses.ConstantsClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;

public class MainForm extends JFrame {
    private Journal journal;
    private TablePanel tablePanel;
    private ButtonPanel buttonPanel;
    private TrayIcon tray;
    private ImageIcon icon;
    private SystemTray systemTray = SystemTray.getSystemTray();
    private static MainForm instance;
    private TaskSender taskSender = TaskSender.getInstance();
    private ClientNetworkFacade clientFacade = ClientNetworkFacade.getInstance();
    private ClientCommandSender commandSender = ClientCommandSender.getInstance();
    private JLabel usernameLabel = new JLabel("Your status is Client. You logged as: ");

    public MainForm() {
        super("Task Scheduler");
        instance = this;
        this.journal = new Journal();
        try {
            icon = new ImageIcon(ParserProperties.getInstance().getProperties(ConstantsClass.MAIN_FORM_ICON));
        } catch (IllegalPropertyException e) {
            JOptionPane.showMessageDialog(MainForm.this, "Illegal value of property",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupted or missing",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        tablePanel = new TablePanel(this);
        tablePanel.setData(new LinkedList<>());
        tablePanel.refresh();
        buttonPanel = new ButtonPanel(this);
        buttonPanel.setJTable((tablePanel.getTable()));

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
                                    try {
                                        commandSender.sendDeleteCommand(builder.toString(), clientFacade.getDataOutputStream());
                                    } catch (UnsuccessfulCommandActionException e) {
                                        JOptionPane.showMessageDialog(MainForm.this,
                                                "Could not send delete command!",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                    }
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
                                    commandSender.sendDeleteCommand(builder.toString(), clientFacade.getDataOutputStream());
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
                    clientFacade.finish();
                    System.exit(0);
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
    public void setJournal(Journal journal) {
        if (journal != null) { // который пришел с команды
            this.journal = journal;
            tablePanel.setData(this.journal.getTasks());
            tablePanel.refresh();
            buttonPanel.setJTable(tablePanel.getTable());
        } else
            JOptionPane.showMessageDialog(MainForm.this,
                    "Incorrect journal!",
                    "Error", JOptionPane.ERROR_MESSAGE);
    }

    public Journal getJournal() {
        return journal;
    }

    public static MainForm getInstance() {
        return instance;
    }
}
