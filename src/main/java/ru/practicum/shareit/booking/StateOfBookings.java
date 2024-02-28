package ru.practicum.shareit.booking;

import java.util.Optional;

public enum StateOfBookings {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<StateOfBookings> from(String state) {
        for (StateOfBookings value : StateOfBookings.values()) {
            if (value.name().equals(state)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
