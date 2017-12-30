package server;

import constants.ConstantsClass;
import server.exceptions.IllegalPropertyException;
import server.gui.authforms.AuthForm;
import server.properties.ParserProperties;

import javax.swing.*;
import java.io.IOException;

public class StartServer {

    public static void main(String[] args) {
        try {
            ParserProperties.getInstance().getProperties(ConstantsClass.MAIN_FORM_ICON);
        }
        catch (IllegalPropertyException e) {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupt or missing!. The application will be closed",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (IOException e) { //todo vlla multicatching
            JOptionPane.showMessageDialog(null, "The configuration file is corrupt or missing!. The application will be closed",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }
}
