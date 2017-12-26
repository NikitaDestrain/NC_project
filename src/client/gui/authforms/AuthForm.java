package client.gui.authforms;

import client.gui.UserContainer;
import client.network.ClientNetworkFacade;
import client.commandprocessor.ClientCommandSender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AuthForm extends JFrame {
    private JButton okButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registrationButton;
    private ClientNetworkFacade clientFacade;
    private static AuthForm instance;
    private ClientCommandSender commandSender = ClientCommandSender.getInstance();
    private UserContainer userContainer = UserContainer.getInstance();

    public AuthForm() {
        super("Authorization");
        instance = this;
        okButton = new JButton("Sign in");
        registrationButton = new JButton("Sign up");
        loginField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля

        layoutComponents();

        okButton.addActionListener((ActionEvent e) -> {
            sendData();
        });

        registrationButton.addActionListener((ActionEvent e) -> {
            this.dispose();
            callSignUpForm();
        });

        clientFacade = ClientNetworkFacade.getInstance();
        if (!clientFacade.isAlive()) clientFacade.start();

        setSize(new Dimension(320, 230));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(
                        null, "Do you really want to close the app?",
                        "Warning!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setVisible(false);
    }

    private void layoutComponents() {
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        Border titleBorder = BorderFactory.createTitledBorder("Enter your login and password");
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

        ///////////////// OK and Cancel buttons an registration button
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(okButton, gc);
        buttonsPanel.add(registrationButton, gc);


        //Dimension btnSize = cancelButton.getPreferredSize();
        //okButton.setPreferredSize(btnSize);

        ///////Adding panels to frame
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }


    public static AuthForm getInstance() {
        return instance;
    }

    private void callSignUpForm() {
        SignUpForm signUpForm = SignUpForm.getInstance();
        if (signUpForm == null) signUpForm = new SignUpForm();
        signUpForm.setVisible(true);
    }

    private void sendData() {
        if (loginField.getText() == null || loginField.getText().length() == 0
                || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect login or password!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (clientFacade.getDataOutputStream() == null)
                JOptionPane.showMessageDialog(null,
                        "Server is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                userContainer.setUsername(loginField.getText());
                commandSender.sendSignInCommand(loginField.getText(),
                        String.valueOf(passwordField.getPassword()),
                        clientFacade.getDataOutputStream());
            }
        }
    }

    public void showUnsuccessfulAuthMessage() {
        JOptionPane.showMessageDialog(null,
                "User with such login and password does not exists! You should sign up now!",
                "Error", JOptionPane.ERROR_MESSAGE);
        this.dispose();
        callSignUpForm();
    }
}
