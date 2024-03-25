package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto) {
        log.debug("POST server new user {}", userDto);
        User user = new User(null, userDto.getName(), userDto.getEmail());
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.debug("DELETE server user with id {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message","User with id " + userId + " has been deleted"));
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.debug("GET server all users ");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.debug("PATCH server update user with id {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.debug("GET server user with id {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
