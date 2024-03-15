package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemResponseDto addNewItem(@RequestBody ItemCreationDto itemCreationDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Post server item  user {}", userId);
        return service.addItem(itemCreationDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestBody ItemCreationDto itemCreationDto,
                                      @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long itemId) {
        log.debug("Path server item  with id {} user {} ", itemId, userId);
        return service.updateItem(itemCreationDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("GET server item  with id {}", itemId);
        return service.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam Integer from,
                                            @RequestParam Integer size) {
        log.debug("GET server all items of user with id {}", userId);
        return service.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam String text,
                                        @RequestParam Integer from,
                                        @RequestParam Integer size) {
        log.debug("GET server search {}", text);
        return service.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody CommentCreationDto comment,
                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId) {
        log.debug("Post server add comment:{} of user with id {} ", comment.getText(), userId);
        return ResponseEntity.ok(service.addComment(comment, userId, itemId));

    }

}
