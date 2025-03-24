package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        UserDao userService = new UserServiceImpl();
        userService.createUsersTable();  // Создаем новую таблицу

        // Добавляем пользователей
        userService.saveUser("Tonny", "Tester", (byte) 25);
        userService.saveUser("Harry", "Hacker", (byte) 30);
        userService.saveUser("Herman", "Johnson", (byte) 28);
        userService.saveUser("Emma", "Williams", (byte) 35);

        // Получаем и выводим всех пользователей
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст.");
        } else {
            System.out.println("Список пользователей:");
            for (User user : users) {
                System.out.printf("ID: %d | Имя: %s | Фамилия: %s | Возраст: %d%n",
                        user.getId(), user.getName(), user.getLastName(), user.getAge());
            }
        }

        userService.dropUsersTable();
    }
}
