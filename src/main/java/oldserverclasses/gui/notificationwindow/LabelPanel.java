package oldserverclasses.gui.notificationwindow;

import javax.swing.*;
import java.awt.*;

public class LabelPanel extends JPanel {
    private JLabel name;
    private JLabel description;

    public LabelPanel() {
        name = new JLabel();
        description = new JLabel();
        name.setFont(new Font("Verdana", Font.BOLD, 18));
        name.setVerticalAlignment(SwingConstants.CENTER);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        description.setVerticalTextPosition(SwingConstants.CENTER);
        description.setFont(new Font("Verdana", Font.PLAIN, 13));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(name);
        add(description);
        Dimension dimension = getPreferredSize();
        dimension.height = 120;
        setPreferredSize(dimension);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }
}
