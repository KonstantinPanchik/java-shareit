package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Long requestId;
    List<CommentDto> comments;

    @Data
    @Builder
    public static class BookingDto {
        Long id;
        LocalDateTime start;
        LocalDateTime end;
        Long bookerId;

    }

    @Data
    @Builder
    public static class CommentDto {
        Long id;
        String text;
        LocalDateTime created;
        String authorName;

    }


}



