package client.gui.authforms;

import client.network.ClientNetworkFacade;
import client.commandprocessor.CommandSender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.lang.Thread.sleep;

public class AuthForm extends JFrame {
    private JButton okButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton registrationButton;
    private ClientNetworkFacade clientFacade;
    private static AuthForm instance;

    public AuthForm() {
        super("Authorization");
        instance = this;
        okButton = new JButton("OK");
        registrationButton = new JButton("Registration");
        loginField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля

        layoutComponents();

        okButton.addActionListener((ActionEvent e)->{
            sendData();
        });

        registrationButton.addActionListener((ActionEvent e) -> {
            this.dispose();
            callSignUpForm();
        });

        clientFacade = ClientNetworkFacade.getInstance();
        clientFacade.start();

        setSize(new Dimension(320,230));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(false);
    }

    private void layoutComponents() {
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        Border titleBorder = BorderFactory.createTitledBorder("Enter your login and password");
        int space = 15;
        Border spaceBorder = BorderFactory.createEmptyBorder(space,space,space,space);

        controlsPanel.setBorder(BorderFactory.createCompoundBorder(spaceBorder,titleBorder ));

        controlsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        Insets rightPadding = new Insets(0,0,0,10);

        ///// Login field and label

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.4;

        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        controlsPanel.add(new JLabel("Login:"),gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        controlsPanel.add(loginField,gc);

        ///// Password field and label
        gc.gridy++;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        controlsPanel.add(new JLabel("Password:"),gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        controlsPanel.add(passwordField,gc);

        ///////////////// OK and Cancel buttons an registration button
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(registrationButton, gc);
        buttonsPanel.add(okButton,gc);


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
            JOptionPane.showMessageDialog( null,
                    "Incorrect login or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            if (clientFacade.getDataOutputStream() == null)
                JOptionPane.showMessageDialog( null,
                        "Server is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            else
                CommandSender.sendSignInCommand(loginField.getText(),
                        String.valueOf(passwordField.getPassword()),
                        clientFacade.getDataOutputStream());
        }
    }

    public void showUnsuccessfulAuthMessage() {
        JOptionPane.showMessageDialog( null,
                "User with such login and password does not exists!",
                "Error", JOptionPane.ERROR_MESSAGE);
        this.dispose();
        callSignUpForm();
    }

    /*private void connection() {
        String login, password;
        login= this.loginField.getText();
        password= String.valueOf(this.passwordField.getPassword());

        if((login.length()==0)|| (password.length()==0))
        {
            JOptionPane.showMessageDialog(this, "Enter login/password", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            ClientNetworkFacade cnf = new ClientNetworkFacade();
            cnf.start();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (cnf.getDataOutputStream() == null) {
                JOptionPane.showMessageDialog(this, "Server is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    CommandSender.sendSignInCommand(login, new PasswordEncoder().encode(password), cnf.getDataOutputStream());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            //при успешной авторизации Сервер отправляет команду об успешной авторихации
            //открываем окно журнала вызывается после прочтения команды Complete Auth в InputCommandHendler
        }
    }*/
}
