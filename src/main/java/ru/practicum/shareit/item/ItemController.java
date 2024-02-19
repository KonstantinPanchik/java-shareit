package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validationGroup.AdvanceInfo;
import ru.practicum.shareit.validationGroup.BasicInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemResponseDto addNewItem(@RequestBody @Validated(value = BasicInfo.class) ItemCreationDto itemCreationDto,
                                      @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Post item  user {}", userId);
        return service.addItem(itemCreationDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestBody @Validated(value = AdvanceInfo.class) ItemCreationDto itemCreationDto,
                                      @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                      @PathVariable Long itemId) {
        log.info("Path item  with id {} user {} ", itemId, userId);
        return service.updateItem(itemCreationDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("GET item  with id {}", itemId);
        return service.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAllItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("GET all items of user with id {}", userId);
        return service.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam String text) {
        log.info("GET search {}", text);
        return service.search(text.toLowerCase());
    }

    @PostMapping("/{itemId}/comment")
    public ItemResponseDto.CommentDto addComment(@RequestBody @Valid Comment comment,
                                                 @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                 @PathVariable Long itemId) {
        return service.addComment(comment, userId, itemId);

    }

}
