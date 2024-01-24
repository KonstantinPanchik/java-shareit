package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@Validated
public class ItemDto {
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

    Long request;

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder().
                name(item.getName()).
                description(item.getDescription()).
                available(item.isAvailable()).
                request(item.getRequest()).
                build();
    }


}