package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.util.List;


public interface BookingService {

    Booking addBooking(Long bookerId, BookItemRequestDto bcd);

    Booking setAppove(Long userId, Long bookingId, Boolean approved);

    Booking getBookingOfBooker(Long userId, Long bookingId);

    List<Booking> getAllBookingOfBooker(Long bookerId, BookingState state, Integer from, Integer size);

    List<Booking> getBookingOfOwner(Long ownerId, BookingState state, Integer from, Integer size);


}
