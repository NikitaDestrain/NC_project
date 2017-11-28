package gui.taskwindow;
import java.awt.*;

import javax.swing.*;

public class ErrDialog extends JDialog {

    private JFrame owner;
    private String s_dialog;

    public ErrDialog(TaskWindow owner, String s_dialog) {
        super(owner, s_dialog, true);
        this.s_dialog = s_dialog;
        this.owner = owner;
        setLocationRelativeTo(null);
        setSize(260, 160);
        initComponents();
        setVisible(true); // отобразить диалог

    }

    private void initComponents() {

        JPanel panel2 = new JPanel();

        panel2.add(new JLabel(s_dialog));
        add(panel2, BorderLayout.NORTH);

    }
}
