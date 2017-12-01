package properties;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;

public class ParserProperties
{
    private static  String PATH_TO_CONFIG = "src/properties/Config.properties";
    private static ParserProperties instance = new ParserProperties();
    private static Properties props;


    private ParserProperties()
    {
        props = new Properties();
    }


    public static synchronized String  getProperties(String key) throws IOException {


       FileInputStream fin = new FileInputStream(PATH_TO_CONFIG);

       props.load(fin);
       fin.close();

       return props.getProperty(key);

   }
}






