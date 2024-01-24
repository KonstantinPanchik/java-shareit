package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRepositoryMem implements ItemRepository {

    private Map<Long, Item> items;
    private long creatorId;

    ItemRepositoryMem() {
        items = new HashMap<>();
    }

    @Override
    public Item addItem(ItemDto itemDto, long userId) {


        Item item = Item.builder()
                .id(getNewId())
                .name(itemDto.getName())
                .user(userId)
                .description(itemDto.getDescription())
                .request(itemDto.getRequest())
                .available(true)
                .build();
        items.put(item.getId(), item);
        log.info("Item saved {}", item);
        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long userId, Long itemId) {
        Item item = Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        if (item.getUser() != userId) {
            log.warn("Wrong user id {} and item owner id {}", userId, item.getUser());
            throw new AccessIsDeniedException("Вы не можете редактировать, чужие предметы!");
        }
        log.info("Item updated {}", item);
        return item.updateNotNullFromDto(itemDto);
    }

    @Override
    public Item getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return items.values().stream().filter(item -> item.getUser() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        log.info("method search {}", text);
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                        && item.isAvailable())
                .collect(Collectors.toList());
    }

    private long getNewId() {
        return ++creatorId;
    }
}
