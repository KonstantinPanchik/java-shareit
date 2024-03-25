package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody ItemRequest itemRequest,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("POST server add request {} by user {}", itemRequest.getDescription(), userId);
        return ResponseEntity.ok(itemRequestService.addRequest(itemRequest, userId));
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("GET server requests of user {}", userId);
        return ResponseEntity.ok(itemRequestService.getRequests(userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("GET server request {} of user {}", requestId, userId);
        return ResponseEntity.ok(itemRequestService.getRequest(requestId, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(required = false, defaultValue = "20") Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("GET server all requests by user {}", userId);
        return ResponseEntity.ok(itemRequestService.getRequestsNotCurrentUser(from, size, userId));
    }

}
