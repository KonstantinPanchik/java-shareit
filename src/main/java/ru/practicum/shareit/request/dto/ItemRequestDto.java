package ru.practicum.shareit.request.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
public class ItemRequestDto {

    Long id;

    String description;

    LocalDateTime created;

    List<ItemDto> answers;

    @Data
    @Builder
    public static class ItemDto {
        Long id;
        String name;
        String description;
        Long requestId;
        Boolean available;
    }

}

