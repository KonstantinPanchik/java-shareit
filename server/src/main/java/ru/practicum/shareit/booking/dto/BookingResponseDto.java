package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseDto {

    long id;

    LocalDateTime start;

    LocalDateTime end;

    Status status;

    User booker;

    ItemResponseDto item;


}


