package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity addUser(@RequestBody @Valid User user) {
        log.info("POST new user {}", user);
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        log.info("DELETE user with id {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with id " + userId + " has been deleted");
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        log.info("GET all users ");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("PATCH update user with id {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable Long userId) {
        log.info("GET user with id {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
