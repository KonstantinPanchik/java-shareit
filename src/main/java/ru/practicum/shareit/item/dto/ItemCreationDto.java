package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Builder
@Validated
public class ItemCreationDto {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 200)
    String name;
    @NotNull
    @NotBlank
    @Size(max = 500)
    String description;
    @NotNull
    Boolean available;

    ItemRequest request;
}