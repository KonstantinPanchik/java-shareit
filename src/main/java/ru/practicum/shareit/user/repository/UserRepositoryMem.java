package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class UserRepositoryMem implements UserRepository {

    private final Map<Long, User> users;

    private long creatorId;

    UserRepositoryMem() {
        users = new HashMap<>();
    }


    @Override
    public User addUser(User user) {
        throwIfEmailExist(user.getEmail());
        user.setId(getNewId());
        users.put(user.getId(), user);
        log.info("User {} was added successfully", user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
        log.info("User {} was deleted successfully", userId);
    }

    @Override
    public User getUser(Long userId) {
        User user = Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + "не существует"));
        log.info("User got successfully {}", user);
        return user;
    }

    @Override
    public User updateUser(Long userId, UserDto update) {
        User user = Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + "не существует"));
        String newEmail = update.getEmail();
        String newName = update.getName();
        if ((newEmail != null) && !(newEmail.isBlank()) && !(user.getEmail().equals(newEmail))) {
            throwIfEmailExist(newEmail);
            user.setEmail(newEmail);
        }
        if ((newName != null) && !(newName.isBlank())) {
            user.setName(newName);
        }
        log.info("User update successfully {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


    private long getNewId() {
        return ++creatorId;
    }

    private boolean isEmailAvailable(String email) {
        boolean result = true;
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                result = false;
                break;
            }
        }
        log.info("Email {} is free");
        return result;
    }

    private void throwIfEmailExist(String email) {
        if (!isEmailAvailable(email)) {
            log.error("Email {} is occupied", email);
            throw new EmailAlreadyExistException("Пользователь с Email " + email + "Уже существует");
        }
    }

    @Override
    public boolean isUserExist(long userId) {
        return users.containsKey(userId);
    }
}
