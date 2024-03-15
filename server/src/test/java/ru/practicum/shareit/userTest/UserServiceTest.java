package ru.practicum.shareit.userTest;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {


    @Autowired
    UserService userService;

    @AfterEach
    void cleanDb() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userService.deleteUser(user.getId());
        }
    }

    @Test
    void shouldAddUser() {
        User user = new User(null, "userName1", "username1@ya.ru");
        assertNull(user.getId());
        userService.addUser(user);
        assertNotNull(user.getId());
    }

    @Test
    void shouldUpdateUser() {
        User user = new User(null, "userName1", "username1@ya.ru");
        assertNull(user.getId());
        userService.addUser(user);
        assertNotNull(user.getId());


        UserDto userDto = UserDto.builder()
                .name("updatedUserName")
                .build();

        User updatedName = userService.updateUser(user.getId(), userDto);

        assertEquals(updatedName.getName(), "updatedUserName");
        assertEquals(updatedName.getEmail(), "username1@ya.ru");

        UserDto userDto2 = UserDto.builder()
                .email("new@mail.ru")
                .build();

        User updatedEmail = userService.updateUser(user.getId(), userDto2);
        assertEquals(updatedEmail.getName(), "updatedUserName");
        assertEquals(updatedEmail.getEmail(), "new@mail.ru");

    }

    @Test
    void shouldNotAddUserWithSameName() {
        User user = new User(null, "userName1", "username1@ya.ru");
        User user2 = new User(null, "userName2", "username1@ya.ru");
        userService.addUser(user);
        assertThrows(Exception.class, () -> userService.addUser(user2));
    }

    @Test
    void shouldGetUser() {
        User user = new User(null, "userName1", "username1@ya.ru");

        User savedUser = userService.addUser(user);

        User gotUser = userService.getUser(user.getId());

        assertEquals(savedUser, gotUser);
    }

    @Test
    void shouldNotGetUserWrongId() {
        User user = new User(null, "userName1", "username1@ya.ru");

        userService.addUser(user);

        assertThrows(NotFoundException.class, () -> userService.getUser(user.getId() + 5));

    }

    @Test
    void shouldDeleteUser() {
        User user = new User(null, "userName1", "username1@ya.ru");

        userService.addUser(user);

        userService.deleteUser(user.getId());

        assertThrows(NotFoundException.class, () -> userService.getUser(user.getId()));

    }

    @Test
    void shouldReturnList() {
        User user = new User(null, "userName1", "username1@ya.ru");
        User user1 = new User(null, "userName2", "username2@ya.ru");
        User user2 = new User(null, "userName3", "username3@ya.ru");
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);

        List<User> result = userService.getAllUsers();
        assertEquals(result.size(), 3);

    }


}
