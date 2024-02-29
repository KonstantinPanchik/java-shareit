package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity addRequest(@RequestBody @Valid ItemRequest itemRequest,
                                     @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.addRequest(itemRequest, userId));
    }

    @GetMapping
    public ResponseEntity getRequests(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.getRequests(userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity getRequest(@PathVariable Long requestId,
                                     @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.getRequest(requestId, userId));
    }

    @GetMapping("/all")
    //todo валидация значений и получение запросов созданных другими пользователями
    public ResponseEntity getAllRequests(@RequestParam(required = false, defaultValue = "0") @Min(0L) Integer from,
                                         @RequestParam(required = false, defaultValue = "20") @Min(1L) Integer size,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(itemRequestService.getRequestsNotCurrentUser(from, size, userId));
    }

}
