package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


public class ItemMapper {

    public static ItemResponseDto toResponseDto(Item item) {

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .requestId(getRequestId(item))
                .comments(commentDtos(item))
                .description(item.getDescription())
                .available(item.getAvailable()).build();
    }

    private static List<ItemResponseDto.CommentDto> commentDtos(Item item) {
        return item.getComments()
                .stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private static Long getRequestId(Item item) {
        if (item.getRequest() != null) {
            return item.getRequest().getId();
        } else {
            return null;
        }
    }

    public static ItemResponseDto toResponseDto(Item item,
                                                ItemResponseDto.BookingDto last,
                                                ItemResponseDto.BookingDto next) {

        ItemResponseDto itemResponseDto = toResponseDto(item);
        itemResponseDto.setLastBooking(last);
        itemResponseDto.setNextBooking(next);
        return itemResponseDto;
    }

    public static Item updateNotNullFromDto(@NotNull ItemCreationDto itemCreationDto, @NotNull Item item) {

        String newName = itemCreationDto.getName();
        if (newName != null && !(newName.isBlank())) {
            item.setName(newName);
        }
        String newDescription = itemCreationDto.getDescription();
        if (newDescription != null && !(newDescription.isBlank())) {
            item.setDescription(newDescription);
        }
        Boolean newAvailable = itemCreationDto.getAvailable();
        if (newAvailable != null) {
            item.setAvailable(newAvailable);
        }
        return item;
    }

    public static ItemResponseDto.CommentDto toCommentDto(Comment comment) {
        ItemResponseDto.CommentDto dto = ItemResponseDto.CommentDto.builder()
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .id(comment.getId())
                .created(comment.getCreated())
                .build();
        return dto;
    }

    public static Item fromCreatingDto(ItemCreationDto itemCreationDto) {
        Item item = new Item();
        item.setName(itemCreationDto.getName());
        item.setDescription(itemCreationDto.getDescription());
        item.setAvailable(itemCreationDto.getAvailable());
        return item;
    }
}
