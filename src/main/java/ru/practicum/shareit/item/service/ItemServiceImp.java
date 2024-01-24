package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(ItemDto itemDto, Long userId) {

        if (!(userRepository.isUserExist(userId))) {
            log.error("User {} notFound", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не существует");
        }
        return itemRepository.addItem(itemDto, userId);
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long userId, Long itemId) {

        if (!(userRepository.isUserExist(userId))) {
            log.error("User {} notFound", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не существует");
        }
        return itemRepository.updateItem(itemDto, userId, itemId);
    }

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.getById(itemId);
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        if (!(userRepository.isUserExist(userId))) {
            log.warn("User {} notFound", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не существует");
        }
        return itemRepository.getUserItems(userId);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            log.warn("text is empty");
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }
}
