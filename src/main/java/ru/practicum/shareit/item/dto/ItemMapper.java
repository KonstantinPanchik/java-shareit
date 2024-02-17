package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


public class ItemMapper {


    public static ItemResponseDto toResponseDto(Item item) {

        List<ItemResponseDto.CommentDto> commentDtos = item.getComments().//fffff
                stream().
                map(ItemMapper::toCommentDto).
                collect(Collectors.toList());

        return ItemResponseDto.builder().id(item.getId()).
                name(item.getName()).
                comments(commentDtos).
                description(item.getDescription()).
                available(item.getAvailable()).build();
    }

    public static ItemResponseDto toResponseDto(Item item,
                                                ItemResponseDto.BookingDto last,
                                                ItemResponseDto.BookingDto next) {
        List<ItemResponseDto.CommentDto> commentDtos = item.getComments().//fffff
                stream().
                map(ItemMapper::toCommentDto).
                collect(Collectors.toList());

        return ItemResponseDto.builder().id(item.getId()).
                name(item.getName()).
                description(item.getDescription()).
                available(item.getAvailable()).
                lastBooking(last).
                nextBooking(next).
                comments(commentDtos).
                build();
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
        ItemResponseDto.CommentDto dto = ItemResponseDto.CommentDto.builder().
                text(comment.getText()).
                authorName(comment.getAuthor().getName()).
                id(comment.getId()).created(comment.getCreated()).
                build();
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
