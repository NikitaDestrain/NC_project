package Properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Prop
{
    //класс для хранения ключ-значение и считывания конфигурационного файла Config.properties
    private static final String PATH_TO_CONFIG = "src/Properties/Config.properties"; //путь к файлу с параметрами
    public String PATH_TO_JOURNAL;

    public  Prop() throws IOException {
        Properties props = new Properties();

         FileInputStream fin = new FileInputStream(PATH_TO_CONFIG);

            props.load(fin); //связали объект с айловым потоком
            this.PATH_TO_JOURNAL=props.getProperty("PATH_TO_JOURNAL");


    }

}
