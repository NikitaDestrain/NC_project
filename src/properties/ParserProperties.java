package properties;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;

public class ParserProperties //todo к сожалению, это не синглетон. инстанс создается, но не используется.
{
    private static  String PATH_TO_CONFIG = "src/properties/Config.properties";//todo не храните проперти в пакетах исходного кода.
    // Проперти это что-то, что может поменять конечный пользователь. Они должны лежать отдельно, конечному пользователю нет нужны лезть в сорцы
    private static ParserProperties instance = new ParserProperties();
    private static Properties props;


    private ParserProperties()
    {
        props = new Properties();
    }


    public static synchronized String  getProperties(String key) throws IOException { //todo каждый раз, когда нужно получить значение по ключу, мы заново загружаем весь файл.
        // А если у нас не одна пропертя, а 500? Весь смысл парсера пропертей в том, чтобы предоставить простой программный доступ к пропертям, которые хранятся где-то извне.
        // Стандартный подход - при запуске приложения один раз прочитать файл, сохранить результаты в переменную и потом предоставлять всему приложению быстрый доступ к пропертям.


       FileInputStream fin = new FileInputStream(PATH_TO_CONFIG);

       props.load(fin);
       fin.close();

       return props.getProperty(key);

   }
}






