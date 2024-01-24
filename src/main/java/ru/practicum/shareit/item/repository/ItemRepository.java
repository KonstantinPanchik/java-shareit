package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item addItem(ItemDto itemDto,long userId);



    Item updateItem(ItemDto itemDto, Long userId, Long itemId);

    Item getById(Long itemId);

    List<Item> getUserItems(Long userId);

    List<Item> search(String search);
}
