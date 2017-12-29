package server.properties;

import server.exceptions.IllegalPropertyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A class that allows you to read the configuration file. It also provides globally access to the server.exceptions.properties described in the configuration file
 */

public class ParserProperties {
    /**
     * Path to the configuration file
     */
   private static String PATH_TO_CONFIG = "data/config.properties";
    /**
     * Object of current class
     */
    private static ParserProperties instance;
    /**
     * Object helper class for reading and displaying server.exceptions.properties
     */
    private Properties props;

    private ParserProperties() throws IOException {
        this.props = new Properties();
        FileInputStream fin = new FileInputStream(PATH_TO_CONFIG);
        this.props.load(fin);
        fin.close();
    }


    /**
     * A method that allows you to get an object of this class
     * @return class object<b> ParserProperties </b>  if the configuration file was read successfully, else return <b>null</b>
     */
    public static ParserProperties getInstance() throws IOException{
        if(instance == null)
        {

            instance = new ParserProperties();
            //todo vlla э не, так не пойдет. Возвращать null - это вообще подло. Это 100% приведет к NPE в каком-нибудь рандомном месте кода.
            // если уж мы словили IOException при парсинге пропертей - его нужно обработать.
            // Я вижу два варианта: либо написать в лог и выдать что-то дефолтное, либо пробросить выше и корректно завершить приложение
        }
        return instance;
    }

    /**
     * The method allows you to get the property found by the <b>key</b>
     * @param key key corresponding to a certain property
     * @return property corresponding to the specified <b>key</b>
     * @throws IllegalPropertyException  if no property is found that matches the specified <b>key</b>
     */
    public  synchronized String getProperties(String key) throws IllegalPropertyException {
        String s = props.getProperty(key);
        if (s == null) {
            throw new IllegalPropertyException();
        } else {
            return s;
        }
    }
}






