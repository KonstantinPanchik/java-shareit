package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {


    User addUser(User user);

    void deleteUser(Long userId);
    User getUser(Long userId);

    User updateUser(Long userId, UserDto userDto);

    List<User> getAllUsers();

}
