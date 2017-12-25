package server.gui.authforms;

import client.commandprocessor.CommandSender;
import client.gui.UserContainer;
import client.network.ClientNetworkFacade;
import server.commandproccessor.User;
import server.controller.Controller;
import server.gui.mainform.MainForm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignUpForm extends JFrame {
    private JButton okButton;
    private JButton clearButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private static SignUpForm instance;
    private Controller controller = Controller.getInstance();

    public SignUpForm() {
        super("Registration");
        instance = this;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        okButton = new JButton("OK");
        clearButton = new JButton("Clear");
        loginField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля
        confirmPasswordField = new JPasswordField(10);
        confirmPasswordField.setEchoChar('*');

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
        String login, password, password2;
        login = this.loginField.getText();
        password = String.valueOf(this.passwordField.getPassword());
        password2 = String.valueOf(this.confirmPasswordField.getPassword());

        if ((login.length() == 0) || (password.length() == 0) || (password2.length() == 0)) {
            JOptionPane.showMessageDialog(this,
                    "Fill up all the fields!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!(password.equals(password2))) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else if (controller.isSuchLoginExists(login)) {
            JOptionPane.showMessageDialog(this,
                    "User with such login already exists!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            controller.addUser(new User(login, password));
            MainForm mainForm = MainForm.getInstance();
            if (mainForm == null) mainForm = new MainForm();
            mainForm.setUsername(login);
            mainForm.setJournal(controller.getJournal());
            mainForm.setVisible(true);
        }
    }
}
