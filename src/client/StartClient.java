package client;

import client.gui.authforms.AuthForm;
import client.properties.ParserProperties;
import constants.ConstantsClass;
import server.exceptions.IllegalPropertyException;

import javax.swing.*;
import java.io.IOException;

public class StartClient {

    public static void main(String[] args) {
        try {
            ParserProperties.getInstance().getProperties(ConstantsClass.MAIN_FORM_ICON);
            ParserProperties.getInstance().getProperties(ConstantsClass.NOTIF_SOUND);
        }
        catch (IllegalPropertyException e)
        {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupt or missing!. The application will be closed",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The configuration file is corrupt or missing!. The application will be closed",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }
}