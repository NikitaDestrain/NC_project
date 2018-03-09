package oldserverclasses.gui.authforms;

import javax.swing.*;

public class AuthForm extends JFrame {
    /*
    private JButton okButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registrationButton;
    private Controller controller = Controller.getInstance();
    private UserAuthorizer authorizer = UserAuthorizer.getInstance();
    private ServerProcessor serverProcessor;
    private static AuthForm instance;
    private PasswordEncoder encoder = PasswordEncoder.getInstance();

    public AuthForm() {
        super("Authorization");
        serverProcessor = ServerProcessor.getInstance();
        instance = this;
        okButton = new JButton("Sign in");
        registrationButton = new JButton("Sign up");
        loginField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля
        serverProcessor.startServer();

        layoutComponents();

        okButton.addActionListener((ActionEvent e) -> {
            signIn();
        });

        registrationButton.addActionListener((ActionEvent e) -> {
            this.dispose();
            callSignUpForm();
        });

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

    private void signIn() {
        String password = null;
        try {
            password = encoder.encode(String.valueOf(passwordField.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not perform this action!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (loginField.getText() == null || loginField.getText().length() == 0
                || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(null,
                    "Incorrect login or password!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!authorizer.isUserDataCorrect(loginField.getText(),
                password)) {
            if (JOptionPane.showConfirmDialog(null,
                    "User with such login and password does not exists! Do you want to sign up now?",
                    "Error", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new SignUpForm().setVisible(true);
                this.dispose();
            }
        } else {
            MainForm mainForm = MainForm.getInstance();
            if (mainForm == null) mainForm = new MainForm();
            mainForm.setUsername(loginField.getText());
            Journal journal = controller.getJournal();
            IDGenerator.getInstance(journal.getMaxId());
            mainForm.setJournal(journal);
            mainForm.setVisible(true);
            this.dispose();
        }
    }*/
}
