package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Users createUser(String name, String email, int age) {
        log.info("Сервис: создание пользователя name={}, email={}, age={}", name, email, age);
        Users user = new Users(name, email, age);
        userDao.save(user);
        log.info("Сервис: пользователь создан id={}", user.getId());
        return user;
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        log.info("Сервис: поиск пользователя по id={}", id);
        return userDao.findById(id);
    }

    @Override
    public List<Users> getAllUsers() {
        log.info("Сервис: получение всех пользователей");
        return userDao.findAll();
    }

    @Override
    public Optional<Users> updateUser(Long id, String newName, String newEmail, Integer newAge) {
        log.info("Сервис: обновление пользователя id={} (newName={}, newEmail={}, newAge={})",
                id, newName, newEmail, newAge);

        Optional<Users> userOpt = userDao.findById(id);
        if (userOpt.isEmpty()) {
            log.info("Сервис: пользователь для обновления не найден id={}", id);
            return Optional.empty();
        }

        Users user = userOpt.get();

        if (newName != null) {
            user.setName(newName);
        }
        if (newEmail != null) {
            user.setEmail(newEmail);
        }
        if (newAge != null) {
            user.setAge(newAge);
        }

        userDao.update(user);
        log.info("Сервис: пользователь обновлен id={}", id);
        return Optional.of(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        log.info("Сервис: удаление пользователя id={}", id);
        Optional<Users> userOpt = userDao.findById(id);
        if (userOpt.isEmpty()) {
            log.info("Сервис: пользователь для удаления не найден id={}", id);
            return false;
        }
        userDao.deleteById(id);
        log.info("Сервис: пользователь удален id={}", id);
        return true;
    }
}
