package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestService;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid ItemRequestCreationDto itemRequest,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestService.addRequest(itemRequest, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestService.getRequest(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(required = false, defaultValue = "0") @Min(0L) Integer from,
                                                 @RequestParam(required = false, defaultValue = "20") @Min(1L) Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return itemRequestService.getRequestsNotCurrentUser(from, size, userId);
    }

}

