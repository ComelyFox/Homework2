package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import org.example.dao.UserDao;
import org.example.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() { }

    @Test
    void testCreateUser() {
        String name = "Alice";
        String email = "alice@example.com";
        int age = 25;

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        Users created = userService.createUser(name, email, age);

        verify(userDao, times(1)).save(userCaptor.capture());
        Users saved = userCaptor.getValue();

        assertEquals(name, saved.getName());
        assertEquals(email, saved.getEmail());
        assertEquals(age, saved.getAge());

        assertEquals(name, created.getName());
        assertEquals(email, created.getEmail());
        assertEquals(age, created.getAge());
    }

    @Test
    void testGetUserById() {
        Long id = 1L;
        Users user = new Users("Bob", "bob@example.com", 30);
        user.setId(id);

        when(userDao.findById(id)).thenReturn(Optional.of(user));

        Optional<Users> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(userDao, times(1)).findById(id);
    }

    @Test
    void testGetUserById_NotFound() {
        Long id = 2L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        Optional<Users> result = userService.getUserById(id);

        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findById(id);
    }

    @Test
    void testGetAllUsers() {
        List<Users> users = Arrays.asList(
                new Users("A", "a@example.com", 20),
                new Users("B", "b@example.com", 21)
        );
        when(userDao.findAll()).thenReturn(users);

        List<Users> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertSame(users, result);
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        Users existing = new Users("Old", "old@example.com", 40);
        existing.setId(id);

        when(userDao.findById(id)).thenReturn(Optional.of(existing));

        Optional<Users> result = userService.updateUser(id, "New", "new@example.com", 41);

        assertTrue(result.isPresent());
        Users updated = result.get();
        assertEquals("New", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(41, updated.getAge());

        verify(userDao, times(1)).findById(id);
        verify(userDao, times(1)).update(existing);
    }

    @Test
    void testUpdateUser_NotFound() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        Optional<Users> result = userService.updateUser(id, "New", "new@example.com", 41);

        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findById(id);
        verify(userDao, never()).update(any());
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        Users existing = new Users("Name", "name@example.com", 20);
        existing.setId(id);

        when(userDao.findById(id)).thenReturn(Optional.of(existing));

        boolean deleted = userService.deleteUser(id);

        assertTrue(deleted);
        verify(userDao, times(1)).findById(id);
        verify(userDao, times(1)).deleteById(id);
    }

    @Test
    void testDeleteUser_NotFound() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        boolean deleted = userService.deleteUser(id);

        assertFalse(deleted);
        verify(userDao, times(1)).findById(id);
        verify(userDao, never()).deleteById(anyLong());
    }
}