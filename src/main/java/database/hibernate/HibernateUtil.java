package database.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory; //todo vlla очень опасная инициализация. DONE
    // static final поля инициализируются во время первой загрузки класса
    // Если вдруг во время выполнения метода buildSessionFactory упадет ошибка - она в лучшем случае сделает недоступным класс,
    // в худшем - вызывет ошибку во время деплоя приложения и оно вообще не развернется
    // Такая инициализация - мина замедленноо действия и в реальных enterprise-приложениях черевата большими проблемами

    private static SessionFactory buildSessionFactory() throws ExceptionInInitializerError {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() throws ExceptionInInitializerError {
        if (sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }
}
