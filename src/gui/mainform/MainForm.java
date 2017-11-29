package gui.mainform;

import gui.taskwindow.TaskWindow;
import controller.Controller;
import controller.SerializeDeserialize;
import model.Journal;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Date;

public class MainForm extends JFrame {
    private JFileChooser fileChooser;
    private SerializeDeserialize journalBackup;
    private Controller controller;// = Controller.getInstance();
    private Journal journal;
    private TablePanel tablePanel;
    private ButtonPanel buttonPanel;
    private TrayIcon tray;
    private ImageIcon icon = new ImageIcon("icon.png");
    private SystemTray systemTray = SystemTray.getSystemTray();
    private static MainForm instance;

    public MainForm() {
        super("Task Scheduler");
        instance = this;
        fileChooser = new JFileChooser();
        journalBackup = new SerializeDeserialize();
        this.journal = new Journal();

        tablePanel = new TablePanel(this);
        tablePanel.setData(this.journal.getTasks());
        tablePanel.refresh();
        buttonPanel = new ButtonPanel(this);
        buttonPanel.setJtable((tablePanel.getTable()));

        buttonPanel.setTableListener(new TableListener() {
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
                                for (int i = 0; i < rows.length; i++) {
                                    Task task = journal.getTasks().get(rows[i]);
                                    controller.removeTask(task.getId());
                                    updateJournal();
                                    buttonPanel.setJtable((tablePanel.getTable()));
                                    for (int j = i + 1; j < rows.length; j++) {
                                        rows[j]--;
                                    }
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
                                for (int i = 0; i < rows.length; i++) {
                                    journal.removeTask(rows[i]);
                                    tablePanel.refresh();
                                    tablePanel.setData(journal.getTasks());
                                    buttonPanel.setJtable((tablePanel.getTable()));
                                    for (int j = i + 1; j < rows.length; j++) {
                                        rows[j]--;
                                    }
                                }
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
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if (action == JOptionPane.OK_OPTION) {
                    try {
                        journalBackup.writeJournal(journal);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file ",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    //System.out.println("Closing");
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
        setVisible(true);
    }

    private void removeFromTray() {
        systemTray.remove(tray);
    }

    private void addToTray() {
        try {
            systemTray.add(tray);
            //tray.displayMessage("Свернулся", "В трей", TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menu.add(fileMenu);

        JMenuItem exportJournal = new JMenuItem("Export journal...");
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
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (action == JOptionPane.OK_OPTION) {
                try {
                    journalBackup.writeJournal(this.journal);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file ",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
            }
        });

        exportJournal.addActionListener((ActionEvent e) -> {

            try {
                journalBackup.writeJournal(journal);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file ",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        importJournal.addActionListener((ActionEvent e) -> {
            try {
                this.journal = journalBackup.readJournal();
                tablePanel.setData(journal.getTasks());
                tablePanel.refresh();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(MainForm.this, "Could not load journal from file",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return menu;
    }

    /**
     * Sets journal to be represented at this <code>mainform</code>
     * @param journal object with tasks for representation
     */
    public void setJournal(Journal journal) {
        if (journal != null) {
            controller = Controller.getInstance();
            for(Task t : journal.getTasks()) {
                controller.addTaskFromBackup(t);
            }
            this.journal = controller.getJournal();
            tablePanel.setData(this.journal.getTasks());
            tablePanel.refresh();
        }
    }

    public static MainForm getInstance() {
        return instance;
    }

    public void updateJournal() {
        controller = Controller.getInstance();
        this.journal = controller.getJournal();
        tablePanel.setData(this.journal.getTasks());
        tablePanel.refresh();
    }
}
