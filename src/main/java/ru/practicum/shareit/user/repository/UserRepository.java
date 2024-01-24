package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {


    User addUser(User user);

    void deleteUser(Long userId);
    User getUser(Long userId);

    User updateUser(Long userId, UserDto userDto);

    List<User> getAllUsers();

    boolean isUserExist(long userId);


}
