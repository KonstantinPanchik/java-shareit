package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {


    public static User updateFromDto(UserDto userDto, User user) {
        String newName = userDto.getName();
        if (newName != null && !(newName.isBlank())) {
            user.setName(userDto.getName());
        }
        String newEmail = userDto.getEmail();
        if (newEmail != null && !(newEmail.isBlank())) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }
}
