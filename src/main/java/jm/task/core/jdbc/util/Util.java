package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class Util {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/myapp";
    private static final String DB_USERNAME = "michaelsolodeynikov";
    private static final String DB_PASSWORD = "PASSWORD";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            //Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return connection;
    }

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            // Настройки подключения к БД (пример для MySQL)
            configuration.setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            configuration.setProperty(Environment.URL, "jdbc:mysql://localhost:3306/myapp");
            configuration.setProperty(Environment.USER, "michaelsolodeynikov");
            configuration.setProperty(Environment.PASS, "PASSWORD");
            configuration.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty(Environment.SHOW_SQL, "true");
            configuration.setProperty(Environment.HBM2DDL_AUTO, "update");

            // Регистрация классов-сущностей
            configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

}
