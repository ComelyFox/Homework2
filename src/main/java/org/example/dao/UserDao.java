package org.example.dao;

import org.example.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    // Создание пользователя
    void save(Users user);

    // Чтение по id
    Optional<Users> findById(Long id);

    // Чтение всех пользователей
    List<Users> findAll();

    // Обновление существующего пользователя
    void update(Users user);

    // Удаление по id
    void deleteById(Long id);
}