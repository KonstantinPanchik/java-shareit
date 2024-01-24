package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {

    long id;
    LocalDateTime start;
    LocalDateTime end;

    long item;

    long booker;

    Status status;
}
