package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;


import ru.practicum.shareit.validationGroup.AdvanceInfo;
import ru.practicum.shareit.validationGroup.BasicInfo;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestBody @Validated(value = BasicInfo.class) ItemCreationDto itemCreationDto,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Post gateway item  user {}", userId);
        return client.addItem(itemCreationDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody @Validated(value = AdvanceInfo.class) ItemCreationDto itemCreationDto,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                             @PathVariable Long itemId) {
        log.info("Patch gateway item  with id {} user {} ", itemId, userId);
        return client.updateItem(itemCreationDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("GET gateway item  with id {}", itemId);
        return client.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                             @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(required = false, defaultValue = "20") @Min(1) Integer size) {
        log.info("GET gateway all items of user with id {}", userId);
        return client.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(required = false, defaultValue = "20") @Min(1) Integer size) {
        log.debug("GET gateway search {}", text);
        return client.search(text.toLowerCase(), from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentCreationDto comment,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                             @PathVariable Long itemId) {
        log.debug("Post gateway add comment: {} user {}", comment.getText(), userId);
        return client.addComment(comment, userId, itemId);

    }

}
