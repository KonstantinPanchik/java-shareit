package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDto itemDto, Long userId);

    Item updateItem(ItemDto itemDto, Long userId, Long itemId);

    Item getItem(Long itemId);

    List<Item> getUserItems(Long userId);

    List<Item> search(String search);
}
