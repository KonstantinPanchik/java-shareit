package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService service;

    @PostMapping
    public Item addNewItem(@RequestBody @Validated ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Post item  user {}", userId);
        return service.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                           @PathVariable Long itemId) {
        log.info("Path item  with id {} user {} ", itemId, userId);
        return service.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        log.info("GET item  with id {}", itemId);
        return service.getItem(itemId);
    }

    @GetMapping
    public List<Item> getAllItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("GET all items of user with id {}", userId);
        return service.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        log.info("GET search {}", text);
        return service.search(text.toLowerCase());
    }
}
