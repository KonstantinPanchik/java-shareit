package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException());
        UserMapper.updateFromDto(userDto, user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public User getUser(Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.orElseThrow(() -> new NotFoundException("User not found"));
        return user;
    }
}
