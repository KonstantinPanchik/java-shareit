package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;
    private String name;
    @NotNull
    @Email
    private String email;
}
