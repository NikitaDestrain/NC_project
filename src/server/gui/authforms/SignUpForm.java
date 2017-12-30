package server.gui.authforms;

import client.commandprocessor.PasswordEncoder;
import server.commandproccessor.User;
import server.controller.Controller;
import server.controller.IDGenerator;
import server.controller.UserAuthorizer;
import server.gui.mainform.MainForm;
import server.model.Journal;
import server.network.ServerNetworkFacade;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.NoSuchAlgorithmException;

public class SignUpForm extends JFrame {
    private JButton okButton;
    private JButton clearButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private static SignUpForm instance;
    private Controller controller = Controller.getInstance();
    private UserAuthorizer authorizer = UserAuthorizer.getInstance();
    private ServerNetworkFacade serverFacade;
    private PasswordEncoder encoder = PasswordEncoder.getInstance();

    public SignUpForm() {
        super("Sign up");
        instance = this;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        okButton = new JButton("OK");
        clearButton = new JButton("Clear");
        loginField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля
        confirmPasswordField = new JPasswordField(10);
        confirmPasswordField.setEchoChar('*');
        serverFacade = ServerNetworkFacade.getInstance();
        if(!serverFacade.isAlive()) serverFacade.start();

        layoutComponents();

        okButton.addActionListener((ActionEvent e) -> {
            registration();
        });

        clearButton.addActionListener((ActionEvent e) -> {
            loginField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        });


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(
                        null, "Do you really want to close the app?",
                        "Warning!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });


        setSize(new Dimension(320, 230));

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(false);
    }

    public static SignUpForm getInstance() {
        return instance;
    }

    private void layoutComponents() {
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        Border titleBorder = BorderFactory.createTitledBorder("Fill up the fields below for registration");
        int space = 15;
        Border spaceBorder = BorderFactory.createEmptyBorder(space, space, space, space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder, titleBorder));

        controlsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        Insets rightPadding = new Insets(0, 0, 0, 10);

        ///// Login field and label

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.4;

        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        controlsPanel.add(new JLabel("Login:"), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        controlsPanel.add(loginField, gc);

        ///// Password field and label
        gc.gridy++;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        controlsPanel.add(new JLabel("Password:"), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        controlsPanel.add(passwordField, gc);

        //// Confirm password field and label

        gc.gridy++;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        controlsPanel.add(new JLabel("Confirm password:"), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        controlsPanel.add(confirmPasswordField, gc);

        ///////////////// OK and Cancel buttons
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(okButton, gc);
        buttonsPanel.add(clearButton, gc);

        Dimension btnSize = clearButton.getPreferredSize();
        okButton.setPreferredSize(btnSize);

        ///////Adding panels to frame
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void registration() {
        String login;
        login = this.loginField.getText();
        String password = null;
        try {
            password = encoder.encode(String.valueOf(this.passwordField.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not perform this action!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        String password2 = null;
        try {
            password2 = encoder.encode(String.valueOf(this.confirmPasswordField.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not perform this action!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if ((login.length() == 0) || (password.length() == 0) || (password2.length() == 0)) {
            JOptionPane.showMessageDialog(this,
                    "Fill up all the fields!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!(password.equals(password2))) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else if (authorizer.isSuchLoginExists(login)) {
            JOptionPane.showMessageDialog(this,
                    "User with such login already exists!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            authorizer.addUser(new User(login, password, -1));
            MainForm mainForm = MainForm.getInstance();
            if (mainForm == null) mainForm = new MainForm();
            mainForm.setUsername(login);
            Journal journal = controller.getJournal();
            IDGenerator.getInstance(journal.getMaxId());
            mainForm.setJournal(journal);
            mainForm.setVisible(true);
            this.dispose();
        }
    }
}
