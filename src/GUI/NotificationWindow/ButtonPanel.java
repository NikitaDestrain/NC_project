package GUI.NotificationWindow;

import controller.Notifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonPanel extends JPanel {
    private JButton later;
    private JButton finish;
    private JButton cancel;

    public ButtonPanel() {
        later = new JButton("Remind later");
        finish = new JButton("Finish task");
        cancel = new JButton("Cancel task");

        setLayout(new FlowLayout());
        add(later);
        add(finish);
        add(cancel);

        Dimension dimension = getPreferredSize();
        dimension.height = 37;
        setPreferredSize(dimension);

        later.addActionListener((ActionEvent e) -> {

        });

        finish.addActionListener((ActionEvent e) -> {

        });

        cancel.addActionListener((ActionEvent e) -> {

        });
    }
}
