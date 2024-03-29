package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto addItem(ItemCreationDto itemCreationDto, Long userId);

    ItemResponseDto updateItem(ItemCreationDto itemCreationDto, Long userId, Long itemId);

    ItemResponseDto getItem(Long itemId, Long userId);

    List<ItemResponseDto> getUserItems(Long userId, Integer from, Integer size);

    List<ItemResponseDto> search(String search, Integer from, Integer size);

    ItemResponseDto.CommentDto addComment(CommentCreationDto comment, Long userId, Long itemId);
}
