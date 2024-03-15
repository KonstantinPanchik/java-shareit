package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder

public class ItemCreationDto {

    String name;

    String description;

    Boolean available;

    Long requestId;
}