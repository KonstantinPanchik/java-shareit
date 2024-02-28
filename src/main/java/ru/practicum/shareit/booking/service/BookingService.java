package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.StateOfBookings;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;


public interface BookingService {

    BookingResponseDto addBooking(Long bookerId, BookingCreationDto bcd);


    BookingResponseDto setAppove(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getBookingOfBooker(Long userId, Long bookingId);

    List<BookingResponseDto> getBookingOfBooker(Long bookerId, StateOfBookings state);

    public List<BookingResponseDto> getBookingOfOwner(Long ownerId, StateOfBookings state);
}