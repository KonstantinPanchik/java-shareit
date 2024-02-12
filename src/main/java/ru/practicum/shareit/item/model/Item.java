package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    long id;
    @Size(min = 3, max = 200)
    String name;
    @Size(max = 500)
    String description;

    boolean available;

    Long user;

    Long request;

    public Item(long id, String name, String description, boolean available, Long user, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;
        this.request = request;
    }

    public Item updateNotNullFromDto(@NotNull ItemDto itemDto) {

        String newName = itemDto.getName();
        if (newName != null && !(newName.isBlank())) {
            this.setName(newName);
        }
        String newDescription = itemDto.getDescription();
        if (newDescription != null && !(newDescription.isBlank())) {
            this.setDescription(newDescription);
        }
        Boolean newAvailable = itemDto.getAvailable();
        if (newAvailable != null) {
            this.setAvailable(newAvailable);
        }
        return this;
    }

}
