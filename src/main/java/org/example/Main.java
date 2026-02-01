package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.Users;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Выберите пункт меню: ");

            try {
                switch (choice) {
                    case 1 -> createUser(userDao);
                    case 2 -> findUserById(userDao);
                    case 3 -> listAllUsers(userDao);
                    case 4 -> updateUser(userDao);
                    case 5 -> deleteUser(userDao);
                    case 0 -> {
                        running = false;
                        log.info("Завершение работы приложения");
                    }
                    default -> System.out.println("Неизвестный пункт меню");
                }
            } catch (Exception ex) {
                System.out.printf("Произошла ошибка: %s\n", ex.getMessage());
                log.error("Ошибка при выполнении операции", ex);
            }
        }

        HibernateUtil.shutdown();
        scanner.close();
    }

    private static void printMenu() {
        System.out.print("""
            
            ==== USER SERVICE ====
            1. Создать пользователя
            2. Найти пользователя по id
            3. Показать всех пользователей
            4. Обновить пользователя
            5. Удалить пользователя
            0. Выход
            """);
    }

    private static void createUser(UserDao userDao) {
        System.out.print("""
                --- Создание пользователя ---
                Имя:\s""");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        int age = readInt("Возраст: ");
        Users user = new Users(name, email, age);
        userDao.save(user);

        System.out.printf("Пользователь создан с id: %s\n", user.getId());
    }

    private static void findUserById(UserDao userDao) {
        System.out.println("--- Поиск пользователя по id ---");
        long id = readLong("id пользователя: ");

        Optional<Users> userOpt = userDao.findById(id);
        if (userOpt.isPresent()) {
            System.out.printf("Найден пользователь: %s\n", userOpt.get());
        } else {
            System.out.printf("Пользователь с id %s не найден\n", id);
        }
    }

    private static void listAllUsers(UserDao userDao) {
        System.out.println("--- Список всех пользователей ---");
        List<Users> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Пользователи отсутствуют");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser(UserDao userDao) {
        System.out.println("--- Обновление пользователя ---");
        long id = readLong("id пользователя для обновления: ");

        Optional<Users> userOpt = userDao.findById(id);
        if (userOpt.isEmpty()) {
            System.out.printf("Пользователь с id %s не найден\n", id);
            return;
        }

        Users user = userOpt.get();
        System.out.printf("Текущие данные: %s\n", user);

        System.out.print("Новое имя (пусто, чтобы не менять): ");
        String newName = scanner.nextLine();
        if (!newName.isBlank()) {
            user.setName(newName);
        }

        System.out.print("Новый email (пусто, чтобы не менять): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isBlank()) {
            user.setEmail(newEmail);
        }

        String ageInput;
        System.out.print("Новый возраст (пусто, чтобы не менять): ");
        ageInput = scanner.nextLine();
        if (!ageInput.isBlank()) {
            try {
                int newAge = Integer.parseInt(ageInput);
                user.setAge(newAge);
            } catch (NumberFormatException ex) {
                System.out.println("Возраст не изменен: некорректное число");
            }
        }

        userDao.update(user);
        System.out.println("Пользователь обновлен");
    }

    private static void deleteUser(UserDao userDao) {
        System.out.println("--- Удаление пользователя ---");
        long id = readLong("id пользователя для удаления: ");

        userDao.deleteById(id);
        System.out.println("Операция удаления выполнена (проверьте лог, был ли пользователь найден)");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите целое число");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Long.parseLong(line.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Введите целое число");
            }
        }
    }
}
