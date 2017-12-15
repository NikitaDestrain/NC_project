package client.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignUpForm extends JFrame {
    private JButton okButton;
    private JButton cancelButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUpForm() {
        super("Registration");
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        loginField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*'); // что отображается при вводе пароля
        confirmPasswordField = new JPasswordField(10);
        confirmPasswordField.setEchoChar('*');

        layoutComponents();

        okButton.addActionListener((ActionEvent e)->{
            JOptionPane.showMessageDialog(null,
                    "OK button", "Button click", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelButton.addActionListener((ActionEvent e) ->{
            JOptionPane.showMessageDialog(null,
                    "Cancel button", "Button click", JOptionPane.INFORMATION_MESSAGE);
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

        Border titleBorder = BorderFactory.createTitledBorder("Fill up the fields below for registration");
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

        //// Confirm password field and label

        gc.gridy++;
        gc.gridx = 0;
        gc.insets = rightPadding;
        gc.anchor = GridBagConstraints.LINE_END;
        controlsPanel.add(new JLabel("Confirm password:"),gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        controlsPanel.add(confirmPasswordField,gc);

        ///////////////// OK and Cancel buttons
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(okButton,gc);
        buttonsPanel.add(cancelButton,gc);

        Dimension btnSize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(btnSize);

        ///////Adding panels to frame
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }
}
