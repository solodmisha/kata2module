package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.List;
import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction(); // Начинаем транзакцию

            String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                lastname VARCHAR(100) NOT NULL,
                age TINYINT NOT NULL
            )
            """;

            // Выполняем SQL-запрос
            session.createNativeQuery(createTableSQL).executeUpdate();

            session.getTransaction().commit(); // Коммитим транзакцию
            System.out.println("Таблица 'User' успешно создана (если не существует).");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица 'users' удалена!");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String insertSQL = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";

        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Выполняем запрос с параметрами, используя позиционные параметры
            int rowsAffected = session.createNativeQuery(insertSQL)
                    .setParameter(1, name)
                    .setParameter(2, lastName)
                    .setParameter(3, age)
                    .executeUpdate();

            if (rowsAffected > 0) {
                System.out.printf("User %s %s добавлен(а) в базу данных!%n", name, lastName);
            }
            session.flush();
            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @Override
    public void removeUserById(long id) {

        String deleteSQL = "DELETE FROM users WHERE id = :id";


        try (Session session = Util.getSessionFactory().openSession()) {

            session.beginTransaction();

            // Выполняем запрос с параметром для ID
            int rowsDeleted = session.createNativeQuery(deleteSQL)
                    .setParameter("id", id)
                    .executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Пользователь с ID " + id + " успешно удален!");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()) {
            // Запрашиваем все пользователей с помощью HQL (Hibernate Query Language)
            String hql = "FROM User";

            users = session.createQuery(hql, User.class).getResultList();

        } catch (Exception e) {
            System.err.println("Ошибка при получении списка пользователей: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public void cleanUsersTable() {
        String truncateSQL = "TRUNCATE TABLE users";

        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createNativeQuery(truncateSQL).executeUpdate();
            session.getTransaction().commit();

            System.out.println("Таблица 'users' очищена!");

        } catch (Exception e) {
            System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
