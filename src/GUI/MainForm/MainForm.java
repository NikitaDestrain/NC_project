package GUI.MainForm;

import controller.SerializeDeserialize;
import model.Journal;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Date;

public class MainForm extends JFrame {
    private JFileChooser fileChooser;
    private SerializeDeserialize journalBackup;
    private Journal journal;
    private TablePanel tablePanel;
    private ButtonPanel buttonPanel;


    public MainForm() {
        super("Task Scheduler");

        fileChooser = new JFileChooser();
        journalBackup = new SerializeDeserialize();
        journal = new Journal();
        Task task = new Task("Test", TaskStatus.Planned, "Test", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        journal.addTask(task);

        tablePanel = new TablePanel();
        tablePanel.setData(journal.getTasks());
        tablePanel.setTableListener((int row) -> {
            journal.removeTask(row);
            tablePanel.refresh();
        });

        buttonPanel = new ButtonPanel();

        setJMenuBar(createMenu());
        setLayout(new BorderLayout());
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
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
                System.exit(0);
            }
        });

        exportJournal.addActionListener((ActionEvent e) -> {
            if (fileChooser.showSaveDialog(MainForm.this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    /**
                     * УТОЧНИТЬ ПО ПОВОДУ КОЛИЧЕСТВА ЖУРНАЛОВ В ФАЙЛЕ
                     */
                    journalBackup.writeJournal(journal, new FileOutputStream(selectedFile));
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file " +
                            selectedFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file " +
                            selectedFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        importJournal.addActionListener((ActionEvent e) -> {
            if (fileChooser.showOpenDialog(MainForm.this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    this.journal = journalBackup.readJournal(new FileInputStream(selectedFile));
                    tablePanel.setData(journal.getTasks());
                    tablePanel.refresh();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file " +
                            selectedFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainForm.this, "Could not save journal to file " +
                            selectedFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return menu;
    }
}
