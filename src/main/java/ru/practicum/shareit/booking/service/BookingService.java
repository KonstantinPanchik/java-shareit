package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StateOfBookings;
import ru.practicum.shareit.booking.dto.BookingCreationDto;

import java.util.List;


public interface BookingService {

    Booking addBooking(Long bookerId, BookingCreationDto bcd);

    Booking setAppove(Long userId, Long bookingId, Boolean approved);

    Booking getBookingOfBooker(Long userId, Long bookingId);

    List<Booking> getAllBookingOfBooker(Long bookerId, StateOfBookings state, Integer from, Integer size);

    List<Booking> getBookingOfOwner(Long ownerId, StateOfBookings state, Integer from, Integer size);


}
