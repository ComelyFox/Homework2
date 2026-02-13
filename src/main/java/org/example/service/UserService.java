package org.example.service;

import org.example.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users createUser(String name, String email, int age);

    Optional<Users> getUserById(Long id);

    List<Users> getAllUsers();

    Optional<Users> updateUser(Long id, String newName, String newEmail, Integer newAge);

    boolean deleteUser(Long id);
}