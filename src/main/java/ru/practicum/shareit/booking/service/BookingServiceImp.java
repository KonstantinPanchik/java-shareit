package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StateOfBookings;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessIsDeniedException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponseDto addBooking(Long bookerId, BookingCreationDto bcd) {

        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(bcd.getItemId()).orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getAvailable() == false) {
            throw new ItemNotAvailableException("This item is not available for booking");
        }

        if (booker.getId().equals(item.getUser().getId())) {
            throw new NotFoundException("You can't book your items");
        }

        Booking booking = BookingMapper.toBookingFromCreation(bcd, booker, item);
        booking = bookingRepository.save(booking);

        return BookingMapper.toBookingResponse(booking);
    }


    @Override
    public BookingResponseDto setAppove(Long userId, Long bookingId, Boolean approved) {
        Optional<Booking> booking1 = bookingRepository.findById(bookingId);
        Booking booking = booking1.orElseThrow(() -> new NotFoundException("Booking has been not found "));
        boolean isOwner = booking.getItem().getUser().getId().equals(userId);
        if (!isOwner) {
            throw new AccessIsDeniedException("Not your item");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ItemNotAvailableException("Item already APPROVED");
        }
        Status newStatus = approved ? Status.APPROVED : Status.REJECTED;
        booking.setStatus(newStatus);
        booking = bookingRepository.save(booking);

        return BookingMapper.toBookingResponse(booking);
    }

    @Override
    public BookingResponseDto getBookingOfBooker(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException());
        if ((!userId.equals(booking.getBooker().getId())) && (!userId.equals(booking.getItem().getUser().getId()))) {
            throw new AccessIsDeniedException("You can't see item with id " + bookingId);
        }

        return BookingMapper.toBookingResponse(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingOfBooker(Long bookerId, StateOfBookings state) {
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Booking> result = new ArrayList<>();
        switch (state) {

            case ALL:
                result = bookingRepository.getAllBookingOfBooker(bookerId);
                break;
            case PAST:
                result = bookingRepository.getPastBookingOfBooker(bookerId, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.getFutureBookingOfBooker(bookerId, LocalDateTime.now());
                break;
            case CURRENT:
                result = bookingRepository.getCurrentBookingOfBooker(bookerId, LocalDateTime.now());
                break;
            default:

                result = bookingRepository.getRejectedOrWaitingBookingOfBooker(bookerId, Status.valueOf(state.toString()));
                break;


        }
        return result.stream()
                .map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingOfOwner(Long bookerId, StateOfBookings state) {
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Booking> result;
        switch (state) {

            case ALL:
                result = bookingRepository.getAllBookingOfOwner(bookerId);
                break;
            case PAST:
                result = bookingRepository.getPastBookingOfOwner(bookerId, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.getFutureBookingOfOwner(bookerId, LocalDateTime.now());
                break;
            case CURRENT:
                result = bookingRepository.getCurrentBookingOfOwner(bookerId, LocalDateTime.now());
                break;
            default:
                result = bookingRepository.getRejectedOrWaitingBookingOfOwner(bookerId, Status.valueOf(state.toString()));
                break;


        }
        return result.stream()
                .map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList());
    }


}
