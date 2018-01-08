package auxiliaryclasses;

import javax.swing.*;

public class MessageBox {
    private static MessageBox instance;

    private MessageBox() {}

    public static MessageBox getInstance() {
        if (instance == null) instance = new MessageBox();
        return instance;
    }

    public synchronized void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error!", JOptionPane.ERROR_MESSAGE);
    }
}
