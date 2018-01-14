package client;

import auxiliaryclasses.MessageBox;
import client.gui.authforms.AuthForm;
import client.properties.ParserProperties;
import auxiliaryclasses.ConstantsClass;
import server.exceptions.IllegalPropertyException;

import javax.swing.*;
import java.io.IOException;

public class StartClient {

    public static void main(String[] args) {
        try {
            ParserProperties.getInstance().getProperties(ConstantsClass.MAIN_FORM_ICON);
            ParserProperties.getInstance().getProperties(ConstantsClass.NOTIF_SOUND);
        } catch (IllegalPropertyException | IOException e) {
            MessageBox.getInstance().showMessage("The configuration file is corrupt or missing!. The application will be closed!");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }
}