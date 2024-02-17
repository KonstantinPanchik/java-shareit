package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingCreationDto {
    @NotNull
    Long itemId;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @FutureOrPresent
    @NotNull
    LocalDateTime end;

    @AssertTrue
    boolean isEndAfterStart() {
        if (start == null || end == null) {
            return false;
        }
        return end.isAfter(start);
    }


}

