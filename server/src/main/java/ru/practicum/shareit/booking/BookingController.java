package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnknownStateException;

import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j

public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Object> createNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestBody BookItemRequestDto bookItemRequestDto) {

        Booking booking = bookingService.addBooking(userId, bookItemRequestDto);
        return ResponseEntity.ok(BookingMapper.toBookingResponse(booking));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApprove(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) {
        Booking booking = bookingService.setAppove(userId, bookingId, approved);
        return ResponseEntity.ok(BookingMapper.toBookingResponse(booking));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingOfBooker(userId, bookingId);
        return ResponseEntity.ok(BookingMapper.toBookingResponse(booking));
    }

    @GetMapping
    public ResponseEntity<Object> getAllMyBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") String state,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new UnknownStateException("Unknown state: " + state));

        return ResponseEntity.ok(bookingService.getAllBookingOfBooker(userId, bookingState, from, size)
                .stream()
                .map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingOfMyItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(required = false, defaultValue = "ALL") String state,
                                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {

        BookingState bookingState = BookingState.valueOf(state);

        return ResponseEntity.ok(bookingService.getBookingOfOwner(userId, bookingState, from, size)
                .stream()
                .map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList()));
    }

}
