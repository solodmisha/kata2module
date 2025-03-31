package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                lastname VARCHAR(100) NOT NULL,
                age TINYINT NOT NULL
            )
            """;

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            System.out.println("Таблица 'users' успешно создана!");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            System.out.println("Таблица 'users' удалена!");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertSQL = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.printf("User %s %s добавлен(а) в базу данных!%n", name, lastName);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {

        String insertSQL = "DELETE FROM users WHERE id = ?;";
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setLong(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Пользователь с ID " + id + " успешно удален!");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); // Гарантируем, что метод не вернет null
        String selectSQL = "SELECT * FROM users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                User user = new User();

                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка пользователей: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("TRUNCATE TABLE users");
            System.out.println("Таблица 'users' очищена!");

        } catch (SQLException e) {
            System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
