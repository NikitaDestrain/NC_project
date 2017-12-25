package client.gui;

import client.commandprocessor.PasswordEncoder;
import client.network.ClientNetworkFacade;
import client.commandprocessor.CommandSender;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static java.lang.Thread.sleep;

public class AuthForm extends JFrame {
    private JButton connectButton;
    private JButton cancelButton;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton jbutt_registration;
    ClientNetworkFacade cnf;

    public AuthForm() {
        super("Authorization");
        connectButton = new JButton("Connect");
        cancelButton = new JButton("Cancel");
        jbutt_registration = new JButton("Registration");
        userField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля

        layoutComponents();

        connectButton.addActionListener((ActionEvent e)->{

           connection();
        });

        cancelButton.addActionListener((ActionEvent e) ->{

           /* JOptionPane.showMessageDialog(null,
                    "Cancel button", "Button click", JOptionPane.INFORMATION_MESSAGE);*/
           dispose();
        });

        jbutt_registration.addActionListener((ActionEvent e) ->
        {

            new SignUpForm().setVisible(true);


        });

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
        controlsPanel.add(userField,gc);

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
        buttonsPanel.add(jbutt_registration, gc);
        buttonsPanel.add(connectButton,gc);
        buttonsPanel.add(cancelButton,gc);


        //Dimension btnSize = cancelButton.getPreferredSize();
        //connectButton.setPreferredSize(btnSize);

        ///////Adding panels to frame
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }



    private void connection() {


        String login, password;
        login= this.userField.getText();
        password= String.valueOf(this.passwordField.getPassword());


        if((login.length()==0)|| (password.length()==0))
        {
            JOptionPane.showMessageDialog(this, "Enter login/password", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            ClientNetworkFacade cnf = new ClientNetworkFacade();
            cnf.start();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (cnf.getDataOutputStream() == null) {
                JOptionPane.showMessageDialog(this, "Server is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {


               try {
                   CommandSender.sendSignInCommand(login, new PasswordEncoder().encode(password), cnf.getDataOutputStream());
                } catch (JAXBException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

            //при успешной авторизации Сервер отправляет команду об успешной авторихации
            //открываем окно журнала вызывается после прочтения команды Complete Auth в InputCommandHendler


        }


    }
}
