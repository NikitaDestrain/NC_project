package properties;

import exceptions.IllegalPropertyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ParserProperties //todo к сожалению, это не синглетон. инстанс создается, но не используется.
{
   private static  String PATH_TO_CONFIG = "Config.properties";//todo не храните проперти в пакетах исходного кода.
    // Проперти это что-то, что может поменять конечный пользователь. Они должны лежать отдельно, конечному пользователю нет нужны лезть в сорцы
    private static ParserProperties instance;
    private static Properties props;



    private ParserProperties() throws IOException //указываем конкретный путь в файлу с конфигурацией
    {

        this.props = new Properties();
        FileInputStream fin = new FileInputStream(PATH_TO_CONFIG);
        this.props.load(fin);
        System.out.println(props.propertyNames());
        fin.close();
    }



    public static  ParserProperties getInstance () {
        if(instance == null)
        {
            try {
                instance = new ParserProperties();
            } catch (IOException e) {
               return null;
            }
        }
    return  instance;
    }

    public  synchronized String getProperties(String key) throws IllegalPropertyException {
        String s = props.getProperty(key);
        if (s == null) {
            throw new IllegalPropertyException();
        } else {
            return s;
        }
    }


    /*  public static synchronized String  getProperties(String key) throws IOException { //todo каждый раз, когда нужно получить значение по ключу, мы заново загружаем весь файл.
        // А если у нас не одна пропертя, а 500? Весь смысл парсера пропертей в том, чтобы предоставить простой программный доступ к пропертям, которые хранятся где-то извне.
        // Стандартный подход - при запуске приложения один раз прочитать файл, сохранить результаты в переменную и потом предоставлять всему приложению быстрый доступ к пропертям.
*/

}






