package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBookingFromCreation(BookingCreationDto bookingCreationDto) {
        Booking booking = Booking.builder().
                start(bookingCreationDto.start).
                end(bookingCreationDto.end).
                status(Status.WAITING).
                build();
        return booking;
    }

    public static Booking toBookingFromCreation(BookingCreationDto bookingCreationDto, User booker, Item item) {
        Booking booking = Booking.builder().
                start(bookingCreationDto.start).
                end(bookingCreationDto.end).
                status(Status.WAITING).
                item(item).
                booker(booker).
                build();
        return booking;
    }

    public static BookingResponseDto toBookingResponse(Booking booking) {
        BookingResponseDto dto = BookingResponseDto.builder().
                id(booking.getId()).
                start(booking.getStart()).
                end(booking.getEnd()).
                status(booking.getStatus()).
                booker(booking.getBooker()).
                item(ItemMapper.toResponseDto(booking.getItem())).
                build();
        return dto;
    }

    public static ItemResponseDto.BookingDto toItemBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return ItemResponseDto.BookingDto.builder().
                bookerId(booking.getBooker().getId()).
                id(booking.getId()).
                start(booking.getStart()).
                end(booking.getEnd()).build();


    }
}

