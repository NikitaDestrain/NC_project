package server;

import auxiliaryclasses.ConstantsClass;
import auxiliaryclasses.MessageBox;
import server.exceptions.IllegalPropertyException;
import server.gui.authforms.AuthForm;
import server.properties.ParserProperties;

import javax.swing.*;
import java.io.IOException;

public class StartServer {

    public static void main(String[] args) {
        try {
            ParserProperties.getInstance().getProperty(ConstantsClass.MAIN_FORM_ICON);
        }
        catch (IllegalPropertyException | IOException e) {
            MessageBox.getInstance().showMessage("The configuration file is corrupt or missing!. The application will be closed!");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            new AuthForm().setVisible(true);
        });
    }
}
