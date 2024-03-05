package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
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

    public BookingCreationDto() {
    }

    public BookingCreationDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}

