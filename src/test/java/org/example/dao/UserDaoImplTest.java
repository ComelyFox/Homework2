package org.example.dao;

import org.example.Main;
import org.example.entity.Users;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)//, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDaoImplTest extends ContainersEnvironment {

    @Autowired
    private UserRepository userDao;


    @Test
    void testSaveAndFindById() {
        Users user = new Users("Integration", "int@example.com", 35);
        userDao.save(user);

        //assertNotNull(user.getId());

        Optional<Users> loaded = userDao.findById(user.getId());
        assertTrue(loaded.isPresent());
        assertEquals("Integration", loaded.get().getName());
        assertEquals("int@example.com", loaded.get().getEmail());
        assertEquals(35, loaded.get().getAge());
    }

    @Test
    void testFindAll() {
        userDao.save(new Users("User1", "u1@example.com", 20));
        userDao.save(new Users("User2", "u2@example.com", 21));

        List<Users> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdate() {
        Users user = new Users("Old", "old@example.com", 40);
        userDao.save(user);

        Long id = user.getId();
        user.setName("New");
        user.setEmail("new@example.com");
        user.setAge(41);

        userDao.save(user);

        Optional<Users> reloaded = userDao.findById(id);
        assertTrue(reloaded.isPresent());
        assertEquals("New", reloaded.get().getName());
        assertEquals("new@example.com", reloaded.get().getEmail());
        assertEquals(41, reloaded.get().getAge());
    }

    @Test
    void testDeleteById() {
        Users user = new Users("ToDelete", "del@example.com", 50);
        userDao.save(user);

        Long id = user.getId();
        userDao.deleteById(id);

        Optional<Users> loaded = userDao.findById(id);
        assertTrue(loaded.isEmpty());
    }
}