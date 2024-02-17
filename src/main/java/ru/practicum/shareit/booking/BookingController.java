package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnknownStateException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity createNewBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                           @RequestBody @Valid BookingCreationDto bookingCreationDto) {

        return ResponseEntity.ok(bookingService.addBooking(userId, bookingCreationDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity setApprove(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return ResponseEntity.ok(bookingService.setAppove(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity getBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                     @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingOfBooker(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity getAllMyBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                          @RequestParam(required = false, defaultValue = "ALL") String state) {
        StateOfBookings stateOfBookings = StateOfBookings.from(state).
                orElseThrow(() -> new UnknownStateException("Unknown state: " + state));

        return ResponseEntity.ok(bookingService.getBookingOfBooker(userId, stateOfBookings));
    }

    @GetMapping("/owner")
    public ResponseEntity getBookingOfMyItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                              @RequestParam(required = false, defaultValue = "ALL") String state) {
        StateOfBookings stateOfBookings = StateOfBookings.from(state)
                        .orElseThrow(() -> new UnknownStateException("Unknown state: " + state));

        return ResponseEntity.ok(bookingService.getBookingOfOwner(userId, stateOfBookings));
    }


}
