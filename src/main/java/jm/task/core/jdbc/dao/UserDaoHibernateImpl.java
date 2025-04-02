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

            session.createNativeQuery(createTableSQL).executeUpdate();
            session.getTransaction().commit();
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
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
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
            users = session.createQuery("FROM User", User.class).getResultList();
        } catch (Exception e) {
            System.err.println("Ошибка при получении списка пользователей: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица 'users' очищена!");
        } catch (Exception e) {
            System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
