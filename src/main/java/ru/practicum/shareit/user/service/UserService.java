package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User getUser(Long userId);

    User updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);

    List<User> getAllUsers();
}
