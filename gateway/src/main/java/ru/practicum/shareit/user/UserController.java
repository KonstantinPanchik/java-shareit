package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validationGroup.AdvanceInfo;
import ru.practicum.shareit.validationGroup.BasicInfo;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated(value = {BasicInfo.class, AdvanceInfo.class}) UserDto userDto) {
        log.info("POST gateway new user {}", userDto);
        return userClient.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("DELETE gateway user with id {}", userId);
        return userClient.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET gateway all users ");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody @Validated(value = {AdvanceInfo.class}) UserDto userDto) {
        log.info("PATCH gateway update user with id {}", userId);
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("GET gateway user with id {}", userId);
        return userClient.getUser(userId);
    }
}

